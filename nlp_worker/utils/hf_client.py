import os
import requests

HF_MODEL = "ProsusAI/finbert"
HF_API_URL = f"https://api-inference.huggingface.co/models/{HF_MODEL}"

def get_headers():
    hf_api_key = os.getenv("HF_API_KEY")
    if not hf_api_key:
        raise ValueError("Missing Hugging Face API key. Please set HF_API_KEY in your environment.")
    return {"Authorization": f"Bearer {hf_api_key}"}

def analyze_sentiment(text: str):
    """
    Analyze sentiment for a single text.
    """
    payload = {"inputs": text}
    response = requests.post(HF_API_URL, headers=get_headers(), json=payload, timeout=15)

    if response.status_code != 200:
        raise RuntimeError(f"Hugging Face API error {response.status_code}: {response.text}")

    result = response.json()[0][0]  # pick first candidate
    return {"label": result["label"], "score": result["score"]}

def analyze_sentiment_batch(texts):
    """
    Analyze sentiment for a batch of texts in one request.
    """
    payload = {"inputs": texts}
    response = requests.post(HF_API_URL, headers=get_headers(), json=payload, timeout=30)

    if response.status_code != 200:
        raise RuntimeError(f"Hugging Face API error {response.status_code}: {response.text}")

    results = []
    for res in response.json():
        best = max(res[0], key=lambda x: x["score"])
        results.append({"label": best["label"], "score": best["score"]})
    return results
