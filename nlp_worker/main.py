import os
import sys
from dotenv import load_dotenv

from nlp_worker.utils import (
    analyze_sentiment_batch,
    analyze_sentiment_text,
    batch_process_texts,
)
from nlp_worker.apis import fetch_news, fetch_reddit_posts

load_dotenv()


def main():
    ticker = "AAPL"

    print("=== Starting sentiment pipeline ===", flush=True)

    # --- Fetch news ---
    print(f"\nFetching news for {ticker}...", flush=True)
    news_articles = fetch_news(ticker, os.getenv("NEWS_API_KEY"))

    if not news_articles:
        print("No news articles fetched. Check NEWS_API_KEY or API quota.", flush=True)
    else:
        news_titles = [article["title"] for article in news_articles[:5] if article.get("title")]
        sentiments = batch_process_texts(news_titles)

        for result in sentiments:
            print(f"[NEWS] {result['text']}", flush=True)
            print(f" → Sentiment: {result['sentiment']} (score={result['confidence']:.4f})", flush=True)

    # --- Fetch reddit posts ---
    print(f"\nFetching Reddit posts for {ticker}...", flush=True)
    reddit_posts = fetch_reddit_posts(ticker, limit=5)

    if not reddit_posts:
        print("No Reddit posts fetched. Check Reddit credentials.", flush=True)
    else:
        reddit_titles = [post["title"] for post in reddit_posts if post.get("title")]
        sentiments = batch_process_texts(reddit_titles)

        for result in sentiments:
            print(f"[REDDIT] {result['text']}", flush=True)
            print(f" → Sentiment: {result['sentiment']} (score={result['confidence']:.4f})", flush=True)

    print("\n=== Finished ===", flush=True)


if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print("❌ Error:", e, file=sys.stderr, flush=True)
