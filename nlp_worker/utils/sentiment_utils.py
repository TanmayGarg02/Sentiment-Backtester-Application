from typing import List, Dict
from .hf_client import analyze_sentiment_api


def parse_hf_response(raw_response: List[Dict]) -> List[Dict]:
    """
    Convert raw Hugging Face API response into a simplified structure.
    """
    results = []

    for prediction_set in raw_response:
        if not prediction_set:
            results.append({"label": "neutral", "score": 0.0})
            continue

        # Sort by confidence score
        sorted_preds = sorted(prediction_set, key=lambda x: x["score"], reverse=True)
        top_pred = sorted_preds[0]

        results.append({
            "label": top_pred["label"].lower(),
            "score": round(top_pred["score"], 4)
        })

    return results


def analyze_sentiment(text: str) -> Dict:
    """
    Convenience function: runs text through HF API and parses response.
    """
    raw = analyze_sentiment_api([text])
    parsed = parse_hf_response(raw)
    return parsed[0]  # single text → return single dict


def batch_process_texts(texts: List[str]) -> List[Dict]:
    """
    Batch version: takes list of texts, returns list of sentiment results.
    """
    raw = analyze_sentiment_api(texts)
    parsed = parse_hf_response(raw)

    return [
        {"text": text, "sentiment": sentiment["label"], "confidence": sentiment["score"]}
        for text, sentiment in zip(texts, parsed)
    ]
