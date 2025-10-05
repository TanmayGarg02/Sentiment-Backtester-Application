package com.example.backend.service;

import com.example.backend.cache.RedisCacheService;
import com.example.backend.dto.BacktestRequest;
import com.example.backend.dto.BacktestResponse;
import com.example.backend.model.BacktestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BacktestService {

    private final RedisCacheService redisCacheService;
    private final BacktestResultService backtestResultService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BacktestResponse runBacktest(BacktestRequest request) {
        String ticker = request.getTicker().toUpperCase();
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();
        String strategyType = request.getStrategyType();
        Map<String, Object> params = request.getParameters() != null ? request.getParameters() : new HashMap<>();

        // Placeholder: in real system we'd fetch OHLCV market data for ticker between dates
        List<Double> dailyPnls;

        switch (strategyType.toUpperCase()) {
            case "SENTIMENT":
                dailyPnls = runSentimentStrategy(ticker, start, end, params);
                break;
            case "MEAN_REVERSION":
                dailyPnls = runMeanReversionStrategy(ticker, start, end, params);
                break;
            case "MOMENTUM":
                dailyPnls = runMomentumStrategy(ticker, start, end, params);
                break;
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strategyType);
        }

        // Compute metrics
        double nav = 1.0;
        for (double r : dailyPnls) {
            nav *= (1 + r);
        }
        double totalReturn = nav - 1.0;
        double avgDailyReturn = dailyPnls.stream().mapToDouble(d -> d).average().orElse(0.0);
        double sharpeRatio = calculateSharpeRatio(dailyPnls);
        double maxDrawdown = calculateMaxDrawdown(dailyPnls);

        // Save to DB
        String paramsJson = null;
        try {
            paramsJson = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        BacktestResult entity = BacktestResult.builder()
                .strategyName(strategyType)
                .ticker(ticker)
                .startDate(start)
                .endDate(end)
                .totalReturn(totalReturn)
                .sharpeRatio(sharpeRatio)
                .maxDrawdown(maxDrawdown)
                .parametersJson(paramsJson)
                .build();

        backtestResultService.saveBacktestResult(entity);

        // Return DTO
        return new BacktestResponse(
                ticker,
                strategyType,
                params,
                start,
                end,
                totalReturn,
                sharpeRatio,
                maxDrawdown,
                avgDailyReturn,
                dailyPnls.size(),
                LocalDateTime.now()
        );
    }

    // --- STRATEGIES ---

    private List<Double> runSentimentStrategy(String ticker, LocalDate start, LocalDate end, Map<String, Object> params) {
        double threshold = params.getOrDefault("sentimentThreshold", 0.5) instanceof Number
                ? ((Number) params.get("sentimentThreshold")).doubleValue()
                : 0.5;

        List<Double> dailyPnls = new ArrayList<>();
        LocalDate date = start;
        double nav = 1.0;

        while (!date.isAfter(end)) {
            Double senti = redisCacheService.getSentimentScore(ticker);
            if (senti == null) senti = 0.0;

            double position = 0.0;
            if (senti > threshold) position = 1.0;
            else if (senti < -threshold) position = -1.0;

            double dailyReturn = 0.001 * position + (Math.random() - 0.5) * 0.002;
            nav *= (1 + dailyReturn);
            dailyPnls.add(dailyReturn);

            date = date.plusDays(1);
        }
        return dailyPnls;
    }

    private List<Double> runMeanReversionStrategy(String ticker, LocalDate start, LocalDate end, Map<String, Object> params) {
        int lookback = params.getOrDefault("lookback", 5) instanceof Number
                ? ((Number) params.get("lookback")).intValue()
                : 5;
        double entryZ = params.getOrDefault("entryZ", 1.0) instanceof Number
                ? ((Number) params.get("entryZ")).doubleValue()
                : 1.0;

        List<Double> dailyPnls = new ArrayList<>();
        Random rand = new Random();
        LocalDate date = start;

        while (!date.isAfter(end)) {
            double zscore = rand.nextGaussian(); // dummy
            double position = 0.0;
            if (zscore > entryZ) position = -1.0; // short if price deviates up
            if (zscore < -entryZ) position = 1.0; // long if price deviates down

            double dailyReturn = 0.0005 * position + (rand.nextDouble() - 0.5) * 0.001;
            dailyPnls.add(dailyReturn);

            date = date.plusDays(1);
        }
        return dailyPnls;
    }

    private List<Double> runMomentumStrategy(String ticker, LocalDate start, LocalDate end, Map<String, Object> params) {
        int lookback = params.getOrDefault("lookback", 20) instanceof Number
                ? ((Number) params.get("lookback")).intValue()
                : 20;

        List<Double> dailyPnls = new ArrayList<>();
        Random rand = new Random();
        LocalDate date = start;

        while (!date.isAfter(end)) {
            double momentum = rand.nextDouble() - 0.5; // dummy signal
            double position = momentum > 0 ? 1.0 : -1.0;

            double dailyReturn = 0.0007 * position + (rand.nextDouble() - 0.5) * 0.0015;
            dailyPnls.add(dailyReturn);

            date = date.plusDays(1);
        }
        return dailyPnls;
    }

    // --- METRICS ---

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
        double peak = 1.0;
        double nav = 1.0;
        double maxDrawdown = 0.0;

        for (double r : returns) {
            nav *= (1 + r);
            if (nav > peak) peak = nav;
            double drawdown = (nav - peak) / peak;
            if (drawdown < maxDrawdown) maxDrawdown = drawdown;
        }
        return maxDrawdown;
    }
}
