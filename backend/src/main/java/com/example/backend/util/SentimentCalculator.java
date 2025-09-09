package com.example.backend.util;

public class SentimentCalculator {

    /**
     * Placeholder sentiment calculation.
     * Replace with a call to your Python ML worker / FinBERT inference.
     *
     * We return a score centered around 0.0 in [-1, +1] so it's symmetric:
     *  - negative values indicate negative sentiment
     *  - positive values indicate positive sentiment
     *
     * Note: in previous examples we used 0..1. Here we use -1..+1 which is more common.
     */
    public static double calculate(String text) {
        if (text == null || text.isBlank()) return 0.0;
        // simple heuristic: presence of "up"/"gain" => positive; "down"/"loss" => negative
        String lower = text.toLowerCase();
        int score = 0;
        if (lower.contains("up") || lower.contains("gain") || lower.contains("bull")) score++;
        if (lower.contains("down") || lower.contains("loss") || lower.contains("bear")) score--;

        // small random jitter so repeated calls don't return identical numbers in demo
        double jitter = (Math.random() - 0.5) * 0.1;
        return Math.max(-1.0, Math.min(1.0, score + jitter));
    }
}
