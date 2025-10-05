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

    public BacktestResult saveBacktestResult(BacktestResult result) {
        return backtestResultRepository.save(result);
    }

    public List<BacktestResult> saveAllBacktestResults(List<BacktestResult> results) {
        return backtestResultRepository.saveAll(results);
    }

    public List<BacktestResult> getResultsByStrategy(String strategyName) {
        return backtestResultRepository.findByStrategyName(strategyName);
    }

    public List<BacktestResult> getResultsByStrategyAndTicker(String strategyName, String ticker) {
        return backtestResultRepository.findByStrategyNameAndTicker(strategyName, ticker);
    }
}
