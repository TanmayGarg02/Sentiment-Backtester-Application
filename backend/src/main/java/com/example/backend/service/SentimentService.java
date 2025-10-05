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

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://127.0.0.1:8000/")
            .build();

    public SentimentResponse getSentiment(String ticker) {
        Double score = redisCacheService.getSentimentScore(ticker);
        String lastUpdated = redisCacheService.getLastUpdatedTimestamp(ticker);

        // 1️⃣ Try cache first
        if (score != null) {
            SentimentData latest = sentimentDataService.getLatestSentiment(ticker);
            if (latest != null) {
                return new SentimentResponse(
                        ticker,
                        latest.getSentimentScore(),
                        latest.getSentimentLabel(),
                        latest.getSource(),
                        latest.getUrl(),
                        lastUpdated != null ? lastUpdated : latest.getDate().toString()
                );
            }
        }

        // 2️⃣ Cache miss → Fetch from Python
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
                entity.setDate(java.time.LocalDateTime.now());
                sentimentDataService.saveSentiment(entity);

                return new SentimentResponse(
                        ticker,
                        score,
                        label,
                        source,
                        url,
                        entity.getDate().toString()
                );
            }
        } catch (Exception e) {
            System.err.println("❌ Python API call failed: " + e.getMessage());
        }

        // 3️⃣ Fallback to DB
        SentimentData latest = sentimentDataService.getLatestSentiment(ticker);
        if (latest != null) {
            return new SentimentResponse(
                    ticker,
                    latest.getSentimentScore(),
                    latest.getSentimentLabel(),
                    latest.getSource(),
                    latest.getUrl(),
                    latest.getDate().toString()
            );
        }

        // 4️⃣ Final fallback
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
