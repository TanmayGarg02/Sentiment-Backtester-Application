package com.example.backend.dto;

import lombok.Data;

@Data
public class SentimentApiResponse {
    private String ticker;      // stock symbol (e.g. AAPL)
    private double score;       // sentiment confidence
    private String label;       // "positive", "neutral", "negative"
    private String source;      // "news" or "reddit"
    private String url;         // post/article link
    private String date;

}
