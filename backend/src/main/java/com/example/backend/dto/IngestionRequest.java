package com.example.backend.dto;

import lombok.Data;

@Data

public class IngestionRequest {
    private String source;   // e.g., "reddit", "twitter", "news"
    private String query;  // raw text
    private String ticker;   // optional, related stock
}