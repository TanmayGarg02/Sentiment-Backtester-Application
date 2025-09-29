package com.example.backend.service;

import com.example.backend.cache.RedisCacheService;
import com.example.backend.dto.SentimentApiResponse;
import com.example.backend.dto.SentimentResponse;
import com.example.backend.model.SentimentData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SentimentService {

    private final RedisCacheService redisCacheService;
    private final SentimentDataService sentimentDataService;

    // WebClient for Python FastAPI
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8000") // Python FastAPI base URL
            .build();

    public SentimentResponse getSentiment(String ticker) {
        Double score = redisCacheService.getSentimentScore(ticker);
        String lastUpdated = redisCacheService.getLastUpdatedTimestamp(ticker);

        String label = "Neutral";
        String source = "N/A";
        String url = null;

        // 1. Try Redis first
        if (score != null) {
            SentimentData latest = sentimentDataService.getLatestSentiment(ticker);
            if (latest != null) {
                label = latest.getSentimentLabel();
                source = latest.getSource();
                url = latest.getUrl();
                if (lastUpdated == null) {
                    lastUpdated = latest.getCollectedAt().toString();
                }
            }
            return new SentimentResponse(ticker, score, label, source, url, lastUpdated);
        }

        // 2. Redis miss → Try Python API
        return callPythonAndPersist(ticker);
    }

    public SentimentResponse refreshSentiment(String ticker) {
        return callPythonAndPersist(ticker);
    }

    private SentimentResponse callPythonAndPersist(String ticker) {
        try {
            SentimentApiResponse apiResp = webClient.get()
                    .uri("/sentiment/{ticker}", ticker)
                    .retrieve()
                    .bodyToMono(SentimentApiResponse.class)
                    .block();

            if (apiResp != null) {
                Double score = apiResp.getScore();
                String label = apiResp.getLabel();
                String source = apiResp.getSource();
                String url = apiResp.getUrl();

                // Save in Redis
                redisCacheService.storeSentimentScore(ticker, score);

                // Save in DB
                SentimentData entity = new SentimentData();
                entity.setTicker(ticker);
                entity.setSentimentScore(score);
                entity.setSentimentLabel(label);
                entity.setSource(source);
                entity.setUrl(url);
                entity.setCollectedAt(java.time.LocalDateTime.now());
                sentimentDataService.saveSentiment(entity);

                return new SentimentResponse(
                        ticker,
                        score,
                        label,
                        source,
                        url,
                        entity.getCollectedAt().toString()
                );
            }
        } catch (Exception e) {
            System.err.println("Python API call failed: " + e.getMessage());
        }

        // If Python failed → fallback to DB latest
        SentimentData latest = sentimentDataService.getLatestSentiment(ticker);
        if (latest != null) {
            return new SentimentResponse(
                    ticker,
                    latest.getSentimentScore(),
                    latest.getSentimentLabel(),
                    latest.getSource(),
                    latest.getUrl(),
                    latest.getCollectedAt().toString()
            );
        }

        // Nothing found → return safe default
        return new SentimentResponse(
                ticker,
                0.0,
                "Neutral",
                "Fallback",
                null,
                null
        );
    }
}
