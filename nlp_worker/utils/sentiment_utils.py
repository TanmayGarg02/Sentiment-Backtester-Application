def parse_result(result):
    """
    Parse HF model result into a normalized sentiment dict.
    Example HF output: {'label': 'positive', 'score': 0.97}
    """
    label = result["label"].lower()
    score = float(result["score"])

    # Normalize: Positive = +score, Negative = -score
    if "positive" in label:
        sentiment_score = score
        sentiment_label = "Positive"
    elif "negative" in label:
        sentiment_score = -score
        sentiment_label = "Negative"
    else:
        sentiment_score = 0.0
        sentiment_label = "Neutral"

    return {
        "sentimentScore": sentiment_score,
        "sentimentLabel": sentiment_label
    }
