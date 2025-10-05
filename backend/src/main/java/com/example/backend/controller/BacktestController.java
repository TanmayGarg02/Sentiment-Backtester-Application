package com.example.backend.controller;

import com.example.backend.dto.BacktestRequest;
import com.example.backend.dto.BacktestResponse;
import com.example.backend.model.BacktestResult;
import com.example.backend.service.BacktestResultService;
import com.example.backend.service.BacktestService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/backtest")
@RequiredArgsConstructor
public class BacktestController {

    private final BacktestService backtestService;
    private final BacktestResultService backtestResultService;
    private final ObjectMapper objectMapper; // used for JSON <-> Map

    @PostMapping("/run")
    public ResponseEntity<BacktestResponse> runBacktest(@RequestBody BacktestRequest request) {
        return ResponseEntity.ok(backtestService.runBacktest(request));
    }

    @PostMapping("/result")
    public ResponseEntity<BacktestResponse> saveResult(@RequestBody BacktestResult result) {
        BacktestResult saved = backtestResultService.saveBacktestResult(result);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PostMapping("/results/batch")
    public ResponseEntity<List<BacktestResponse>> saveBatch(@RequestBody List<BacktestResult> results) {
        List<BacktestResponse> saved = backtestResultService.saveAllBacktestResults(results)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/strategy/{strategyType}")
    public ResponseEntity<List<BacktestResponse>> getByStrategy(@PathVariable String strategyType) {
        List<BacktestResponse> results = backtestResultService.getResultsByStrategy(strategyType)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/strategy/{strategyType}/ticker/{ticker}")
    public ResponseEntity<List<BacktestResponse>> getByStrategyAndTicker(
            @PathVariable String strategyType,
            @PathVariable String ticker) {
        List<BacktestResponse> results = backtestResultService.getResultsByStrategyAndTicker(strategyType, ticker)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    private BacktestResponse toResponse(BacktestResult entity) {
        Map<String,Object> params = null;
        try {
            if (entity.getParametersJson() != null) {
                params = objectMapper.readValue(entity.getParametersJson(), new TypeReference<>() {});
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse parametersJson", e);
        }

        return new BacktestResponse(
                entity.getTicker(),
                entity.getStrategyName(),
                params,
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getTotalReturn() != null ? entity.getTotalReturn() : 0.0,
                entity.getSharpeRatio() != null ? entity.getSharpeRatio() : 0.0,
                entity.getMaxDrawdown() != null ? entity.getMaxDrawdown() : 0.0,
                0.0, // avgDailyReturn not stored in DB
                0,
                entity.getCreatedAt()// days not stored in DB
        );
    }
}
