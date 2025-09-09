package com.example.backend.service;

import com.example.backend.dto.BacktestRequest;
import com.example.backend.dto.BacktestResult;
import com.example.backend.cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BacktestService {

    private final RedisCacheService redisCacheService;

    /**
     * A simple backtest runner:
     * - For each day in the requested window, it fetches today's sentiment (from cache).
     * - Applies a trivial rule: if sentiment > threshold buy, else sell.
     * - Simulates simple PnL returns and returns a result summary.
     *
     * This is intentionally simple: later we replace with a proper BacktestRunner
     * that uses historical market data and execution models.
     */
    public BacktestResult runBacktest(BacktestRequest request) {
        String ticker = request.getTicker().toUpperCase();
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();
        double threshold = request.getSentimentThreshold();

        List<Double> dailyPnls = new ArrayList<>();
        LocalDate date = start;
        double position = 0.0; // 1.0 = long, -1.0 = short, 0 = neutral
        double nav = 1.0; // starting NAV

        while (!date.isAfter(end)) {
            // in this skeleton we treat cached sentiment as "today's sentiment"
            Double senti = redisCacheService.getSentimentScore(ticker);
            if (senti == null) senti = 0.0;

            // simple decision rule
            if (senti > threshold) {
                position = 1.0;
            } else if (senti < -threshold) {
                position = -1.0;
            } else {
                position = 0.0;
            }

            // simulate a tiny random return influenced by sentiment sign (placeholder)
            double dailyReturn = 0.001 * position + (Math.random() - 0.5) * 0.002;
            nav = nav * (1 + dailyReturn);
            dailyPnls.add(dailyReturn);

            date = date.plusDays(1);
        }

        // summary metrics (very basic)
        double totalReturn = nav - 1.0;
        double avgDaily = dailyPnls.stream().mapToDouble(d -> d).average().orElse(0.0);
        return new BacktestResult(ticker, start, end, totalReturn, avgDaily, dailyPnls.size());
    }
}
