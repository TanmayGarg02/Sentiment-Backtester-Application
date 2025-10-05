import os
import sys
import yfinance as yf
from dotenv import load_dotenv

from nlp_worker.utils.sentiment_utils import batch_process_texts
from nlp_worker.apis.news_client import fetch_news
from nlp_worker.apis.reddit_client import fetch_reddit_posts

load_dotenv()


def get_company_name(ticker: str) -> str:
    """
    Fetch company name from ticker using yfinance.
    Falls back to ticker itself if lookup fails.
    """
    try:
        info = yf.Ticker(ticker).info
        return info.get("shortName") or ticker
    except Exception:
        return ticker


def main():
    ticker = "AAPL"
    company_name = get_company_name(ticker)
    query = f"{ticker} OR {company_name}"

    print("=== Starting sentiment pipeline ===", flush=True)
    print(f"Using query: {query}", flush=True)

    # --- Fetch news ---
    print(f"\nFetching news for {query}...", flush=True)
    news_articles = fetch_news(query, limit=5)

    if not news_articles:
        print("No news articles fetched.", flush=True)
    else:
        news_texts = [f"{a['title']} {a.get('description','')}" for a in news_articles]
        news_sentiments = batch_process_texts(news_texts)

        for result in news_sentiments:
            print(f"[NEWS] {result['text']}", flush=True)
            print(f" → {result['sentiment']} (score={result['confidence']:.4f})", flush=True)

    # --- Fetch reddit posts ---
    print(f"\nFetching Reddit posts for {query}...", flush=True)
    reddit_posts = fetch_reddit_posts(query, limit=5)

    if not reddit_posts:
        print("No Reddit posts fetched.", flush=True)
    else:
        reddit_texts = [f"{p['title']} {p.get('body','')}" for p in reddit_posts]
        reddit_sentiments = batch_process_texts(reddit_texts)

        for result in reddit_sentiments:
            print(f"[REDDIT] {result['text']}", flush=True)
            print(f" → {result['sentiment']} (score={result['confidence']:.4f})", flush=True)

    print("\n=== Finished ===", flush=True)


if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print("❌ Error:", e, file=sys.stderr, flush=True)
