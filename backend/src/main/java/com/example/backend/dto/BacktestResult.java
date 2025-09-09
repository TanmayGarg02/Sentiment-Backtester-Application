package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BacktestResult {
    private String ticker;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalReturn;
    private double avgDailyReturn;
    private int days;
}
