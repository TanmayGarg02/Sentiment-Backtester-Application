package com.example.backend.service;

import com.example.backend.dto.SentimentResponse;
import com.example.backend.cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentimentService {

    private final RedisCacheService redisCacheService;

    /**
     * Retrieve the latest sentiment score for the ticker.
     * If not found, return a neutral 0.0 score to avoid nulls.
     */
    public SentimentResponse getSentiment(String ticker) {
        Double score = redisCacheService.getSentimentScore(ticker);
        if (score == null) {
            score = 0.0;
        }
        return new SentimentResponse(ticker, score, redisCacheService.getLastUpdatedTimestamp(ticker));
    }
}
