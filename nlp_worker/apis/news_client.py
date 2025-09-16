"""
News API client for fetching latest financial news articles.
Docs: https://newsapi.org/docs/endpoints/everything
"""

import os
import requests
from typing import List, Dict

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
    api_key = os.getenv("NEWS_API_KEY")

    if not api_key:
        raise ValueError("NEWS_API_KEY not set in environment!")

    params = {
        "q": query,
        "sortBy": "publishedAt",
        "pageSize": limit,
        "apiKey": api_key,
        "language": "en"
    }

    response = requests.get(BASE_URL, params=params, timeout=10)
    response.raise_for_status()

    articles = response.json().get("articles", [])
    simplified = [
        {
            "title": article.get("title"),
            "description": article.get("description"),
            "url": article.get("url")
        }
        for article in articles
    ]

    return simplified
