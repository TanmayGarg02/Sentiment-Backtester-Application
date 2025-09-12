package com.example.backend.controller;

import com.example.backend.dto.SentimentResponse;
import com.example.backend.model.SentimentData;
import com.example.backend.service.SentimentService;
import com.example.backend.service.SentimentDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sentiment")
@RequiredArgsConstructor
public class SentimentController {

    private final SentimentService sentimentService;       // Redis + enrichment
    private final SentimentDataService sentimentDataService; // DB CRUD

    /**
     * Get the latest sentiment for a ticker (from Redis with DB enrichment).
     */
    @GetMapping("/{ticker}")
    public ResponseEntity<SentimentResponse> getSentiment(@PathVariable String ticker) {
        SentimentResponse resp = sentimentService.getSentiment(ticker.toUpperCase());
        return ResponseEntity.ok(resp);
    }

    /**
     * Save a new sentiment record to DB.
     */
    @PostMapping
    public ResponseEntity<SentimentData> saveSentiment(@RequestBody SentimentData sentimentData) {
        return ResponseEntity.ok(sentimentDataService.saveSentiment(sentimentData));
    }

    /**
     * Save multiple sentiment records at once.
     */
    @PostMapping("/batch")
    public ResponseEntity<List<SentimentData>> saveAllSentiments(@RequestBody List<SentimentData> sentiments) {
        return ResponseEntity.ok(sentimentDataService.saveAllSentiments(sentiments));
    }

    /**
     * Get all sentiment records for a ticker within a date range.
     */
    @GetMapping("/{ticker}/history")
    public ResponseEntity<List<SentimentData>> getSentimentHistory(
            @PathVariable String ticker,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(sentimentDataService.getSentimentHistory(ticker.toUpperCase(), start, end));
    }

    /**
     * Get the latest sentiment record for a ticker from DB.
     */
    @GetMapping("/{ticker}/latest")
    public ResponseEntity<SentimentData> getLatestSentiment(@PathVariable String ticker) {
        return ResponseEntity.ok(sentimentDataService.getLatestSentiment(ticker.toUpperCase()));
    }

    /**
     * Delete a sentiment record by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSentiment(@PathVariable Long id) {
        sentimentDataService.deleteSentiment(id);
        return ResponseEntity.noContent().build();
    }
}
