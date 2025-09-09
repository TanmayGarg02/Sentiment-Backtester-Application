package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BacktestRequest {
    private String ticker;
    private LocalDate startDate;
    private LocalDate endDate;
    /**
     * Sentiment threshold to decide long/short. Could be tuned in experiments.
     */
    private double sentimentThreshold = 0.5;
}
