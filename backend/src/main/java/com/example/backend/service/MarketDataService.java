package com.example.backend.service;

import com.example.backend.model.MarketData;
import com.example.backend.repository.MarketDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MarketDataService {

    private final MarketDataRepository marketDataRepository;

    public MarketDataService(MarketDataRepository marketDataRepository) {
        this.marketDataRepository = marketDataRepository;
    }


    public MarketData saveMarketData(MarketData marketData) {
        return marketDataRepository.save(marketData);
    }

    public List<MarketData> saveAllMarketData(List<MarketData> marketDataList) {
        return marketDataRepository.saveAll(marketDataList);
    }

    public List<MarketData> getHistoricalData(String ticker) {
        return marketDataRepository.findByTickerOrderByDateAsc(ticker);
    }

    public List<MarketData> getHistoricalDataInRange(String ticker, LocalDate start, LocalDate end) {
        return marketDataRepository.findByTickerAndDateBetween(ticker, start, end);
    }
}
