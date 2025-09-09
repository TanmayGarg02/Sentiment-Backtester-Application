package com.example.backend.service;

import com.example.backend.dto.IngestionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final MessagingService messagingService;

    /**
     * Ingest data for the provided request. In this skeleton we simulate fetching a piece of text
     * that mentions the ticker and publish it for the NLP worker to analyze.
     */
    public void ingestData(IngestionRequest request) {
        // In production, you'd call Reddit API / Twitter API / News RSS here.
        // For now construct a simple synthetic payload that includes the ticker.
        String syntheticText = buildSyntheticPost(request.getSource(), request.getTicker(), request.getQuery());
        messagingService.publishForAnalysis(request.getTicker().toUpperCase(), syntheticText);
    }

    private String buildSyntheticPost(String source, String ticker, String query) {
        StringBuilder sb = new StringBuilder();
        sb.append("source=").append(source).append(" ");
        sb.append("ticker=").append(ticker.toUpperCase()).append(" ");
        if (query != null && !query.isBlank()) {
            sb.append("query=").append(query).append(" ");
        }
        sb.append("post=This is a sample post discussing ").append(ticker.toUpperCase())
                .append(" and market conditions.");
        return sb.toString();
    }
}
