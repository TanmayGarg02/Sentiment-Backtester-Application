package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentimentResponse {
    private String ticker;
    private double score;
    private String label;   // optional but useful
    private String source;           // optional but useful
    private String lastUpdatedIso;
}
