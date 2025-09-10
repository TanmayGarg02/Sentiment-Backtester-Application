package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "backtest_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BacktestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String strategyName;
    private String ticker;
    private LocalDate startDate;
    private LocalDate endDate;

    private Double totalReturn;
    private Double sharpeRatio;
    private Double maxDrawdown;

    private LocalDateTime createdAt = LocalDateTime.now();
}
