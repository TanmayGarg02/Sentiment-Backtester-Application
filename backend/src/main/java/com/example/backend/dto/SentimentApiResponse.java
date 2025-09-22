package com.example.backend.dto;

import lombok.Data;

@Data
public class SentimentApiResponse {
    private double score;
    private String label;
    private String source;
}
