package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BacktestResponse {
    private String ticker;
    private String strategyName;
    private LocalDate startDate;
    private LocalDate endDate;

    // From DB entity
    private double totalReturn;
    private double sharpeRatio;
    private double maxDrawdown;

    // Computed dynamically
    private double avgDailyReturn;
    private int days;
}
