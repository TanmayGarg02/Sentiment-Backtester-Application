package com.example.backend.service;

import com.example.backend.cache.RedisCacheService;
import com.example.backend.dto.BacktestRequest;
import com.example.backend.dto.BacktestResponse;
import com.example.backend.model.BacktestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BacktestService {

    private final RedisCacheService redisCacheService;
    private final BacktestResultService backtestResultService;

    public BacktestResponse runBacktest(BacktestRequest request) {
        String ticker = request.getTicker().toUpperCase();
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();
        double threshold = request.getSentimentThreshold();

        List<Double> dailyPnls = new ArrayList<>();
        LocalDate date = start;
        double position;
        double nav = 1.0; // starting NAV

        while (!date.isAfter(end)) {
            Double senti = redisCacheService.getSentimentScore(ticker);
            if (senti == null) senti = 0.0;

            // Trading rule
            if (senti > threshold) {
                position = 1.0;
            } else if (senti < -threshold) {
                position = -1.0;
            } else {
                position = 0.0;
            }

            // Simulate daily return (placeholder logic)
            double dailyReturn = 0.001 * position + (Math.random() - 0.5) * 0.002;
            nav = nav * (1 + dailyReturn);
            dailyPnls.add(dailyReturn);

            date = date.plusDays(1);
        }

        // Metrics
        double totalReturn = nav - 1.0;
        double avgDailyReturn = dailyPnls.stream().mapToDouble(d -> d).average().orElse(0.0);
        double sharpeRatio = calculateSharpeRatio(dailyPnls);
        double maxDrawdown = calculateMaxDrawdown(dailyPnls);

        String strategyName = "SentimentThresholdStrategy";

        // Persist entity in Postgres
        BacktestResult entity = BacktestResult.builder()
                .strategyName(strategyName)
                .ticker(ticker)
                .startDate(start)
                .endDate(end)
                .totalReturn(totalReturn)
                .sharpeRatio(sharpeRatio)
                .maxDrawdown(maxDrawdown)
                .createdAt(LocalDateTime.now())
                .build();

        backtestResultService.saveBacktestResult(entity);

        // Return enriched DTO
        return new BacktestResponse(
                ticker,
                strategyName,
                start,
                end,
                totalReturn,
                sharpeRatio,
                maxDrawdown,
                avgDailyReturn,
                dailyPnls.size()
        );
    }

    private double calculateSharpeRatio(List<Double> returns) {
        if (returns.isEmpty()) return 0.0;
        double avg = returns.stream().mapToDouble(d -> d).average().orElse(0.0);
        double variance = returns.stream()
                .mapToDouble(d -> Math.pow(d - avg, 2))
                .average().orElse(0.0);
        double stdDev = Math.sqrt(variance);
        return stdDev == 0 ? 0.0 : avg / stdDev * Math.sqrt(252); // annualized
    }

    private double calculateMaxDrawdown(List<Double> returns) {
        double peak = 0.0;
        double trough = 0.0;
        double maxDrawdown = 0.0;
        double cumulative = 0.0;

        for (double r : returns) {
            cumulative += r;
            if (cumulative > peak) {
                peak = cumulative;
                trough = cumulative;
            }
            if (cumulative < trough) {
                trough = cumulative;
                maxDrawdown = Math.min(maxDrawdown, (trough - peak));
            }
        }
        return maxDrawdown;
    }
}
