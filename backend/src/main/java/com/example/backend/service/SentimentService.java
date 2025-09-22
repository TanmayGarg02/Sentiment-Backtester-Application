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

    // WebClient instance to call Python FastAPI
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8000") // ✅ your FastAPI base URL
            .build();

    public SentimentResponse getSentiment(String ticker) {
        Double score = redisCacheService.getSentimentScore(ticker);
        String lastUpdated = redisCacheService.getLastUpdatedTimestamp(ticker);

        String label = "Neutral";
        String source = "N/A";

        if (score == null) {
            // Call Python FastAPI
            SentimentApiResponse apiResp = webClient.get()
                    .uri("/sentiment/{ticker}", ticker)
                    .retrieve()
                    .bodyToMono(SentimentApiResponse.class)
                    .block();

            if (apiResp != null) {
                score = apiResp.getScore();
                label = apiResp.getLabel();
                source = "PythonNLP";

                // Save to Redis
                redisCacheService.storeSentimentScore(ticker, score);

                // Save to DB
                SentimentData entity = new SentimentData();
                entity.setTicker(ticker);
                entity.setSentimentScore(score);
                entity.setSentimentLabel(label);
                entity.setSource(source);
                entity.setCollectedAt(java.time.LocalDateTime.now());
                sentimentDataService.saveSentiment(entity);
            }
        } else {
            // Cache hit: enrich with DB info if available
            SentimentData latest = sentimentDataService.getLatestSentiment(ticker);
            if (latest != null) {
                label = latest.getSentimentLabel();
                source = latest.getSource();
                if (lastUpdated == null) {
                    lastUpdated = latest.getCollectedAt().toString();
                }
            }
        }

        if (score == null) {
            score = 0.0; // safe default
        }

        return new SentimentResponse(ticker, score, label, source, lastUpdated);
    }
}
