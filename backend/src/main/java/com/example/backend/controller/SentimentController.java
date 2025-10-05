package com.example.backend.controller;

import com.example.backend.dto.SentimentResponse;
import com.example.backend.model.SentimentData;
import com.example.backend.service.SentimentService;
import com.example.backend.service.SentimentDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sentiment")
@RequiredArgsConstructor
public class SentimentController {

    private final SentimentService sentimentService;
    private final SentimentDataService sentimentDataService;

    @GetMapping("/{ticker}")
    public ResponseEntity<SentimentResponse> getSentiment(@PathVariable String ticker) {
        SentimentResponse resp = sentimentService.getSentiment(ticker.toUpperCase());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{ticker}/refresh")
    public ResponseEntity<SentimentResponse> forceRefresh(@PathVariable String ticker) {
        SentimentResponse resp = sentimentService.refreshSentiment(ticker.toUpperCase());
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<SentimentData> saveSentiment(@RequestBody SentimentData sentimentData) {
        return ResponseEntity.ok(sentimentDataService.saveSentiment(sentimentData));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<SentimentData>> saveAllSentiments(@RequestBody List<SentimentData> sentiments) {
        return ResponseEntity.ok(sentimentDataService.saveAllSentiments(sentiments));
    }

    @GetMapping("/{ticker}/history")
    public ResponseEntity<List<SentimentData>> getSentimentHistory(
            @PathVariable String ticker,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(sentimentDataService.getSentimentHistory(ticker.toUpperCase(), start, end));
    }

    /**
     * Returns the latest sentiment entry from DB (raw stored value, without cache/Python).
     * Useful for analytics/backtesting.
     */
    @GetMapping("/{ticker}/latest")
    public ResponseEntity<SentimentData> getLatestSentiment(@PathVariable String ticker) {
        return ResponseEntity.ok(sentimentDataService.getLatestSentiment(ticker.toUpperCase()));
    }

    /**
     * Deletes a sentiment record by id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSentiment(@PathVariable Long id) {
        sentimentDataService.deleteSentiment(id);
        return ResponseEntity.noContent().build();
    }
}
