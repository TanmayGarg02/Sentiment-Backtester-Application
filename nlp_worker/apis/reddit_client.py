import praw
import os


def fetch_reddit_posts(ticker: str, limit: int = 10):
    """
    Fetch Reddit posts mentioning the ticker using PRAW.
    Uses Reddit API credentials from environment variables.
    """

    reddit = praw.Reddit(
        client_id=os.getenv("REDDIT_CLIENT_ID"),
        client_secret=os.getenv("REDDIT_CLIENT_SECRET"),
        user_agent=os.getenv("REDDIT_USER_AGENT")
    )

    posts = []
    subreddit = reddit.subreddit("stocks")  # You can change to 'wallstreetbets', 'investing', etc.

    for submission in subreddit.search(ticker, limit=limit):
        posts.append({
            "title": submission.title,
            "url": submission.url,
            "score": submission.score
        })

    return posts
