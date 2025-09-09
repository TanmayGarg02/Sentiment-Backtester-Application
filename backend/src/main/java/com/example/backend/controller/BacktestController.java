package com.example.backend.controller;

import com.example.backend.dto.BacktestRequest;
import com.example.backend.dto.BacktestResult;
import com.example.backend.service.BacktestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backtest")
@RequiredArgsConstructor
public class BacktestController {

    private final BacktestService backtestService;

    /**
     * Run a backtest with the provided parameters and returns a simple result summary.
     * For the skeleton we run synchronously and return a result object.
     */
    @PostMapping
    public ResponseEntity<BacktestResult> runBacktest(@RequestBody BacktestRequest request) {
        BacktestResult result = backtestService.runBacktest(request);
        return ResponseEntity.ok(result);
    }
}
