package com.example.backend.config;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {
    @Value("${python.api.url:http://localhost:8000}")
    private String pythonApiUrl;

    @Bean
    public WebClient pythonWebClient() {
        return WebClient.builder()
                .baseUrl(pythonApiUrl)
                .build();
    }
}

