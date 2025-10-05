package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentimentResponse {
    private String ticker;
    private double sentimentScore;
    private String sentimentLabel;
    private String source;
    private String url;
    private String lastUpdatedIso;
}
