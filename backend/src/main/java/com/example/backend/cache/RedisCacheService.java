package com.example.backend.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final StringRedisTemplate redisTemplate;
    private static final long TTL_SECONDS = 60 * 60 * 24; // 24 hours default

    public void storeSentimentScore(String ticker, double score) {
        String key = keyForTicker(ticker);
        redisTemplate.opsForValue().set(key, String.valueOf(score), TTL_SECONDS, TimeUnit.SECONDS);
        // store last updated timestamp for debugging/inspection
        redisTemplate.opsForValue().set(key + ":ts", Instant.now().toString(), TTL_SECONDS, TimeUnit.SECONDS);
    }

    public Double getSentimentScore(String ticker) {
        String key = keyForTicker(ticker);
        String val = redisTemplate.opsForValue().get(key);
        if (val == null) return null;
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getLastUpdatedTimestamp(String ticker) {
        String ts = redisTemplate.opsForValue().get(keyForTicker(ticker) + ":ts");
        return ts;
    }

    private String keyForTicker(String ticker) {
        return "sentiment:" + ticker.toUpperCase();
    }
}
