package com.example.backend.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE = "nlp-queue";
    public static final String EXCHANGE = "nlp-exchange";
    public static final String ROUTING_KEY = "nlp.key";

    @Bean
    public Queue nlqQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange nlpExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue nlqQueue, DirectExchange nlpExchange) {
        return BindingBuilder.bind(nlqQueue).to(nlpExchange).with(ROUTING_KEY);
    }
}
