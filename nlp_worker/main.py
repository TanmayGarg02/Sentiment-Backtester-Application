import os
import sys
from dotenv import load_dotenv

from nlp_worker.utils.hf_client import analyze_sentiment_batch
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
        sentiments = analyze_sentiment_batch(news_titles)

        for article, sentiment in zip(news_articles[:5], sentiments):
            print(f"[NEWS] {article['title']}", flush=True)
            print(f" → Sentiment: {sentiment['label']} (score={sentiment['score']:.4f})", flush=True)

    # --- Fetch reddit posts ---
    print(f"\nFetching Reddit posts for {ticker}...", flush=True)
    reddit_posts = fetch_reddit_posts(ticker, limit=5)

    if not reddit_posts:
        print("No Reddit posts fetched. Check Reddit credentials.", flush=True)
    else:
        reddit_titles = [post["title"] for post in reddit_posts if post.get("title")]
        sentiments = analyze_sentiment_batch(reddit_titles)

        for post, sentiment in zip(reddit_posts[:5], sentiments):
            print(f"[REDDIT] {post['title']}", flush=True)
            print(f" → Sentiment: {sentiment['label']} (score={sentiment['score']:.4f})", flush=True)

    print("\n=== Finished ===", flush=True)


if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print("❌ Error:", e, file=sys.stderr, flush=True)
