package com.example.backend.service;

import com.example.backend.model.SentimentData;
import com.example.backend.model.SentimentData;
import com.example.backend.repository.SentimentDataRepository;
import com.example.backend.repository.SentimentDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SentimentDataService {

    private final SentimentDataRepository sentimentRepository;

    public SentimentDataService(SentimentDataRepository sentimentRepository) {
        this.sentimentRepository = sentimentRepository;
    }

    /**
     * Save a single sentiment entry.
     */
    public SentimentData saveSentiment(SentimentData sentiment) {
        return sentimentRepository.save(sentiment);
    }

    /**
     * Save multiple sentiment entries at once (bulk insert from scraper/collector).
     */
    public List<SentimentData> saveAllSentiments(List<SentimentData> sentiments) {
        return sentimentRepository.saveAll(sentiments);
    }

    /**
     * Get sentiment scores for a ticker in a given date range.
     */
    public List<SentimentData> getSentimentHistory(String ticker, LocalDate start, LocalDate end) {
        return sentimentRepository.findByTickerAndDateBetween(ticker, start, end);
    }

    /**
     * Get the most recent sentiment score for a ticker.
     * (Useful when displaying "current market mood" on dashboard).
     */
    public SentimentData getLatestSentiment(String ticker) {
        return sentimentRepository
                .findByTickerOrderByCollectedAtDesc(ticker)
                .orElse(null);
    }
}
