package com.example.backend.controller;

import com.example.backend.dto.SentimentResponse;
import com.example.backend.service.SentimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sentiment")
@RequiredArgsConstructor
public class SentimentController {

    private final SentimentService sentimentService;

    /**
     * Returns the latest cached sentiment score for a ticker.
     * We expose this so frontends / backtester can fetch features quickly.
     */
    @GetMapping("/{ticker}")
    public ResponseEntity<SentimentResponse> getSentiment(@PathVariable String ticker) {
        SentimentResponse resp = sentimentService.getSentiment(ticker.toUpperCase());
        return ResponseEntity.ok(resp);
    }
}
