from fastapi import FastAPI, HTTPException
from nlp_worker.utils import analyze_sentiment
from nlp_worker.apis.news_client import fetch_news
from nlp_worker.apis.reddit_client import fetch_reddit_posts
from nlp_worker import config

app = FastAPI(title="Sentiment NLP Worker", version="1.0.0")

@app.get("/")
def root():
    return {"message": "NLP Worker API is running 🚀"}


@app.get("/sentiment/")
def get_sentiment(text: str):
    """
    Analyze sentiment of a given text.
    Example: /sentiment/?text=Apple%20stock%20is%20falling
    """
    try:
        result = analyze_sentiment(text)
        return {"text": text, "sentiment": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/news/{ticker}")
def get_news_sentiment(ticker: str, limit: int = 5):
    """
    Fetch latest financial news for a stock ticker & analyze sentiment.
    Example: /news/AAPL?limit=3
    """
    try:
        news_articles = fetch_news(ticker, config.NEWS_API_KEY, limit=limit)
        results = []
        for article in news_articles:
            # Title + description combined for stronger context
            full_text = f"{article['title']} {article.get('description', '')}"
            sentiment = analyze_sentiment(full_text)
            results.append({
                "title": article["title"],
                "url": article["url"],
                "sentiment": sentiment
            })
        return {"ticker": ticker, "results": results}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/reddit/{ticker}")
def get_reddit_sentiment(ticker: str, limit: int = 5):
    """
    Fetch Reddit posts for a stock ticker & analyze sentiment.
    Example: /reddit/AAPL?limit=3
    """
    try:
        reddit_posts = fetch_reddit_posts(ticker, limit=limit)
        results = []
        for post in reddit_posts:
            # Title + body/selftext combined
            full_text = f"{post['title']} {post.get('body', '')}"
            sentiment = analyze_sentiment(full_text)
            results.append({
                "title": post["title"],
                "url": post["url"],
                "sentiment": sentiment
            })
        return {"ticker": ticker, "results": results}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
