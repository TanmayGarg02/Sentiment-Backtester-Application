from fastapi import FastAPI, Query
from nlp_worker.utils.sentiment_utils import batch_process_texts
from nlp_worker.apis.news_client import fetch_news
from nlp_worker.apis.reddit_client import fetch_reddit_posts
import yfinance as yf
from dotenv import load_dotenv
load_dotenv()


app = FastAPI(title="Sentiment NLP Worker", version="1.0.0")


def get_company_name(ticker: str) -> str:
    try:
        info = yf.Ticker(ticker).info
        return info.get("shortName") or ticker
    except Exception:
        return ticker


@app.get("/")
def root():
    return {"message": "NLP Worker API is running 🚀"}


@app.get("/sentiment/{ticker}")
def get_sentiment(ticker: str, limit: int = 5, threshold: float = Query(0.65, ge=0.0, le=1.0)):
    """
    Fetch news + reddit posts for ticker, analyze sentiment,
    return only results above a given threshold.
    """
    company_name = get_company_name(ticker)
    query = f"{ticker} OR {company_name}"

    results = []

    # NEWS
    news_articles = fetch_news(query, limit=limit)
    if news_articles:
        news_texts = [f"{a['title']} {a.get('description','')}" for a in news_articles]
        sentiments = batch_process_texts(news_texts)
        for art, sent in zip(news_articles, sentiments):
            if sent["confidence"] >= threshold:
                results.append({
                    "source": "news",
                    "title": art["title"],
                    "url": art["url"],
                    "sentimentLabel": sent["sentiment"],
                    "sentimentScore": sent["confidence"]
                })

    # REDDIT
    reddit_posts = fetch_reddit_posts(query, limit=limit)
    if reddit_posts:
        reddit_texts = [f"{p['title']} {p.get('body','')}" for p in reddit_posts]
        sentiments = batch_process_texts(reddit_texts)
        for post, sent in zip(reddit_posts, sentiments):
            if sent["confidence"] >= threshold:
                results.append({
                    "source": "reddit",
                    "title": post["title"],
                    "url": post["url"],
                    "sentimentLabel": sent["sentiment"],
                    "sentimentScore": sent["confidence"]
                })

    return {
        "ticker": ticker,
        "company": company_name,
        "threshold": threshold,
        "results": results
    }
