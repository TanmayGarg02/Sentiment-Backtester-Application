package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class BacktestRequest {
    private String ticker;
    private String strategyType;              // e.g. "SENTIMENT", "MEAN_REVERSION"
    private Map<String, Object> parameters;   // flexible param storage
    private LocalDate startDate;
    private LocalDate endDate;
}
