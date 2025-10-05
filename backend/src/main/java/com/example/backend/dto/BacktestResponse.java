package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class BacktestResponse {
    private String ticker;
    private String strategyName;
    private Map<String, Object> parameters;

    private LocalDate startDate;
    private LocalDate endDate;

    private double totalReturn;
    private double sharpeRatio;
    private double maxDrawdown;

    private double avgDailyReturn;
    private int days;

    private LocalDateTime createdAt;
}
