package com.example.backend.controller;

import com.example.backend.dto.BacktestRequest;
import com.example.backend.dto.BacktestResponse;
import com.example.backend.model.BacktestResult;
import com.example.backend.service.BacktestResultService;
import com.example.backend.service.BacktestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/backtest")
@RequiredArgsConstructor
public class BacktestController {

    private final BacktestService backtestService;
    private final BacktestResultService backtestResultService;

    /**
     * Run a backtest with the provided parameters and return result summary.
     */
    @PostMapping("/run")
    public ResponseEntity<BacktestResponse> runBacktest(@RequestBody BacktestRequest request) {
        return ResponseEntity.ok(backtestService.runBacktest(request));
    }

    /**
     * Save a backtest result manually (useful for testing).
     */
    @PostMapping("/result")
    public ResponseEntity<BacktestResponse> saveResult(@RequestBody BacktestResult result) {
        BacktestResult saved = backtestResultService.saveBacktestResult(result);
        return ResponseEntity.ok(toResponse(saved));
    }

    /**
     * Save multiple backtest results at once.
     */
    @PostMapping("/results/batch")
    public ResponseEntity<List<BacktestResponse>> saveBatch(@RequestBody List<BacktestResult> results) {
        List<BacktestResponse> saved = backtestResultService.saveAllBacktestResults(results)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saved);
    }

    /**
     * Get all results for a given strategy.
     */
    @GetMapping("/strategy/{strategyName}")
    public ResponseEntity<List<BacktestResponse>> getByStrategy(@PathVariable String strategyName) {
        List<BacktestResponse> results = backtestResultService.getResultsByStrategy(strategyName)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    /**
     * Get all results for a given strategy and ticker.
     */
    @GetMapping("/strategy/{strategyName}/ticker/{ticker}")
    public ResponseEntity<List<BacktestResponse>> getByStrategyAndTicker(
            @PathVariable String strategyName,
            @PathVariable String ticker) {
        List<BacktestResponse> results = backtestResultService.getResultsByStrategyAndTicker(strategyName, ticker)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    /**
     * Helper: Convert entity -> DTO
     */
    private BacktestResponse toResponse(BacktestResult entity) {
        return new BacktestResponse(
                entity.getTicker(),
                entity.getStrategyName(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getTotalReturn() != null ? entity.getTotalReturn() : 0.0,
                entity.getSharpeRatio() != null ? entity.getSharpeRatio() : 0.0,
                entity.getMaxDrawdown() != null ? entity.getMaxDrawdown() : 0.0,
                0.0, // avgDailyReturn not stored in DB
                0    // days not stored in DB
        );
    }
}
