package com.example.backend.repository;

import com.example.backend.model.SentimentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SentimentDataRepository extends JpaRepository<SentimentData, Long> {
//    Optional<SentimentData> findFirstByTickerOrderByDateDesc(String ticker);

    @Query(value = "SELECT * FROM sentiment_data WHERE ticker = :ticker ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Optional<SentimentData> findLatestByTicker(@Param("ticker") String ticker);


    List<SentimentData> findByTickerAndDateBetween(String ticker, LocalDateTime start, LocalDateTime end);
}
