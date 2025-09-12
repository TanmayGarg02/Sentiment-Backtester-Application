package com.example.backend.service;

import com.example.backend.dto.SentimentResponse;
import com.example.backend.model.SentimentData;
import com.example.backend.cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentimentService {

    private final RedisCacheService redisCacheService;
    private final SentimentDataService sentimentDataService; // inject DB service

    /**
     * Retrieve the latest sentiment score for the ticker.
     * If Redis has data, use it; else fall back to DB.
     */
    public SentimentResponse getSentiment(String ticker) {
        Double score = redisCacheService.getSentimentScore(ticker);
        String lastUpdated = redisCacheService.getLastUpdatedTimestamp(ticker);

        // Default values
        String label = "Neutral";
        String source = "N/A";

        // If DB has richer info, use it
        SentimentData latest = sentimentDataService.getLatestSentiment(ticker);
        if (latest != null) {
            label = latest.getSentimentLabel();
            source = latest.getSource();
            if (score == null) score = latest.getSentimentScore(); // fallback
            if (lastUpdated == null) lastUpdated = latest.getCollectedAt().toString();
        }

        if (score == null) {
            score = 0.0; // ensure no nulls
        }

        return new SentimentResponse(ticker, score, label, source, lastUpdated);
    }
}
