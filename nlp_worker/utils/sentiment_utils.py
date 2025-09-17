from typing import List, Dict
from .hf_client import analyze_sentiment, analyze_sentiment_batch

def parse_hf_response(raw_response):
    """
    Convert raw Hugging Face API response into a simplified structure.
    Handles both single-dict and list-of-dicts cases.
    """
    results = []

    for prediction_set in raw_response:
        if not prediction_set:
            results.append({"label": "neutral", "score": 0.0})
            continue

        # Case 1: already a dict (e.g., {"label": "neutral", "score": 0.93})
        if isinstance(prediction_set, dict) and "label" in prediction_set:
            results.append({
                "label": prediction_set["label"].lower(),
                "score": round(prediction_set["score"], 4)
            })
            continue

        # Case 2: list of dicts (normal HF response)
        if isinstance(prediction_set, list) and isinstance(prediction_set[0], dict):
            sorted_preds = sorted(prediction_set, key=lambda x: x["score"], reverse=True)
            top_pred = sorted_preds[0]
            results.append({
                "label": top_pred["label"].lower(),
                "score": round(top_pred["score"], 4)
            })
            continue

        # Fallback
        results.append({"label": "neutral", "score": 0.0})

    return results

def analyze_sentiment_text(text: str) -> Dict:
    """
    Wrapper for single-text sentiment analysis.
    """
    raw = analyze_sentiment(text)  # HF client single call
    parsed = parse_hf_response(raw)
    return parsed[0]


def batch_process_texts(texts: List[str]) -> List[Dict]:
    """
    Run batch sentiment analysis and return cleaned results.
    """
    raw = analyze_sentiment_batch(texts)  # HF client batch call
    parsed = parse_hf_response(raw)

    return [
        {"text": text, "sentiment": sentiment["label"], "confidence": sentiment["score"]}
        for text, sentiment in zip(texts, parsed)
    ]
