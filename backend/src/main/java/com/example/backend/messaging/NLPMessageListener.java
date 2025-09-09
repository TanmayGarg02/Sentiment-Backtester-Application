package com.example.backend.messaging;

import com.example.backend.cache.RedisCacheService;
import com.example.backend.util.SentimentCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NLPMessageListener {

    private final RedisCacheService redisCacheService;

    /**
     * Consume messages published by the ingestion service.
     * Message format: "TICKER::text". We split on "::" — robust parsing should be added later.
     */
    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void processMessage(String message) {
        if (message == null || message.isBlank()) return;
        String[] parts = message.split("::", 2);
        if (parts.length < 2) return;

        String ticker = parts[0].toUpperCase();
        String text = parts[1];

        double score = SentimentCalculator.calculate(text);
        redisCacheService.storeSentimentScore(ticker, score);
    }
}
