package com.example.backend.service;

import com.example.backend.model.BacktestResult;
import com.example.backend.repository.BacktestResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BacktestResultService {

    private final BacktestResultRepository backtestResultRepository;

    public BacktestResultService(BacktestResultRepository backtestResultRepository) {
        this.backtestResultRepository = backtestResultRepository;
    }

    /**
     * Save a backtest result (from running a strategy).
     */
    public BacktestResult saveBacktestResult(BacktestResult result) {
        return backtestResultRepository.save(result);
    }

    /**
     * Save multiple backtest results at once (batch run).
     */
    public List<BacktestResult> saveAllBacktestResults(List<BacktestResult> results) {
        return backtestResultRepository.saveAll(results);
    }

    /**
     * Get all results for a given strategy name.
     */
    public List<BacktestResult> getResultsByStrategy(String strategyName) {
        return backtestResultRepository.findByStrategyName(strategyName);
    }

    /**
     * Get results for a specific strategy and ticker.
     */
    public List<BacktestResult> getResultsByStrategyAndTicker(String strategyName, String ticker) {
        return backtestResultRepository.findByStrategyNameAndTicker(strategyName, ticker);
    }
}
