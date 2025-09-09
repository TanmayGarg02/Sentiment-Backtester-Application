package com.example.backend.service;

import com.example.backend.messaging.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publish a text payload for NLP processing. Message format: "TICKER::text".
     * We keep the delimiter simple for the skeleton.
     */
    public void publishForAnalysis(String ticker, String text) {
        String payload = ticker + "::" + text;
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, payload);
    }
}
