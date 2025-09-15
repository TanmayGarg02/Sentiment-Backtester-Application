import os
import requests

# Hugging Face Inference API endpoint for FinBERT
HF_MODEL = "ProsusAI/finbert"   # Best FinBERT for financial sentiment
HF_API_URL = f"https://api-inference.huggingface.co/models/{HF_MODEL}"

# Get API key from environment variable
HF_API_KEY = os.getenv("HF_API_KEY")

if not HF_API_KEY:
    raise ValueError("Missing Hugging Face API key. Please set HF_API_KEY in your environment.")

# HTTP headers for Hugging Face API
HEADERS = {"Authorization": f"Bearer {HF_API_KEY}"}


def analyze_sentiment_api(texts):
    """
    Call Hugging Face Inference API for sentiment analysis.

    Args:
        texts (list[str]): List of input texts (e.g. headlines/news).

    Returns:
        list: Raw response from Hugging Face API (list of list of dicts with label & score).
    """
    payload = {"inputs": texts}
    response = requests.post(HF_API_URL, headers=HEADERS, json=payload)

    if response.status_code != 200:
        raise RuntimeError(
            f"Hugging Face API error {response.status_code}: {response.text}"
        )

    return response.json()
