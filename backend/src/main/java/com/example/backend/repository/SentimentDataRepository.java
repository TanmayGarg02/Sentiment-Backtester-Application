package com.example.backend.repository;

import com.example.backend.model.SentimentData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SentimentDataRepository extends JpaRepository<SentimentData, Long> {
    Optional<SentimentData> findByTickerOrderByCollectedAtDesc(String ticker);

    List<SentimentData> findByTickerAndDateBetween(String ticker, LocalDate start, LocalDate end);
}
