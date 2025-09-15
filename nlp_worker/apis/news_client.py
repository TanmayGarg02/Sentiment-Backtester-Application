"""
News API client for fetching latest financial news articles.
Docs: https://newsapi.org/docs/endpoints/everything
"""

import os
import requests
from typing import List, Dict

NEWS_API_KEY = os.getenv("NEWS_API_KEY", "")

BASE_URL = "https://newsapi.org/v2/everything"


def fetch_news(query: str, limit: int = 5) -> List[Dict]:
    """
    Fetch financial news articles for a given query/ticker.

    Args:
        query (str): Stock ticker or keyword (e.g., "AAPL", "Tesla").
        limit (int): Number of articles to fetch.

    Returns:
        list[dict]: Articles with title, description, url.
    """
    if not NEWS_API_KEY:
        raise ValueError("NEWS_API_KEY not set in environment!")

    params = {
        "q": query,
        "sortBy": "publishedAt",
        "pageSize": limit,
        "apiKey": NEWS_API_KEY,
        "language": "en"
    }

    response = requests.get(BASE_URL, params=params, timeout=10)
    response.raise_for_status()

    articles = response.json().get("articles", [])
    simplified = []

    for article in articles:
        simplified.append({
            "title": article.get("title"),
            "description": article.get("description"),
            "url": article.get("url")
        })

    return simplified
