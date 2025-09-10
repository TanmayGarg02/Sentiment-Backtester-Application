package com.example.backend.repository;

import com.example.backend.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    List<MarketData> findByTickerOrderByDateAsc(String ticker);
    List<MarketData> findByTickerAndDateBetween(String ticker, LocalDate start, LocalDate end);
}
