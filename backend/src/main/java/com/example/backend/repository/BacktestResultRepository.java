package com.example.backend.repository;

import com.example.backend.model.BacktestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BacktestResultRepository extends JpaRepository<BacktestResult, Long> {
    List<BacktestResult> findByTicker(String ticker);
    List<BacktestResult> findByStrategyName(String strategyName);

    List<BacktestResult> findByStrategyNameAndTicker(String strategyName, String ticker);
}
