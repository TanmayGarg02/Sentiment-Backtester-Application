package com.example.backend.controller;

import com.example.backend.model.MarketData;
import com.example.backend.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketDataController {

    private final MarketDataService marketDataService;

    /**
     * Get all market data for a ticker between start and end dates.
     */
    @GetMapping("/{ticker}")
    public ResponseEntity<List<MarketData>> getByTickerAndRange(
            @PathVariable String ticker,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(marketDataService.getHistoricalDataInRange(ticker, start, end));
    }

    /**
     * Insert a new market data row manually (useful for testing).
     */
    @PostMapping
    public ResponseEntity<MarketData> addMarketData(@RequestBody MarketData data) {
        return ResponseEntity.ok(marketDataService.saveMarketData(data));
    }

    /**
     * Get all market data for a ticker (ordered ASC).
     */
    @GetMapping("/{ticker}/all")
    public ResponseEntity<List<MarketData>> getAllByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(marketDataService.getHistoricalData(ticker));
    }
}
