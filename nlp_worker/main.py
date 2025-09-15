import os
from dotenv import load_dotenv

from nlp_worker.utils import analyze_sentiment, batch_process_texts
from nlp_worker.apis import fetch_news, fetch_reddit_posts

load_dotenv()


def main():
    ticker = "AAPL"

    # --- Example: fetch news ---
    print(f"Fetching news for {ticker}...")
    news_articles = fetch_news(ticker, os.getenv("NEWS_API_KEY"))

    for article in news_articles[:3]:
        sentiment = analyze_sentiment(article["title"])
        print(f"[NEWS] {article['title']}")
        print(f" → Sentiment: {sentiment['label']} (score={sentiment['score']:.4f})")

    # --- Example: fetch reddit posts ---
    print(f"\nFetching Reddit posts for {ticker}...")
    reddit_posts = fetch_reddit_posts(ticker, limit=5)

    for post in reddit_posts[:3]:
        sentiment = analyze_sentiment(post["title"])
        print(f"[REDDIT] {post['title']}")
        print(f" → Sentiment: {sentiment['label']} (score={sentiment['score']:.4f})")


if __name__ == "__main__":
    main()
