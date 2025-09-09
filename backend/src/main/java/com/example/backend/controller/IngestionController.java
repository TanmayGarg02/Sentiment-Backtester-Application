package com.example.backend.controller;

import com.example.backend.dto.IngestionRequest;
import com.example.backend.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingestion")
@RequiredArgsConstructor
public class IngestionController {
    private final IngestionService ingestionService;

    @PostMapping
    public ResponseEntity<String> ingest(@RequestBody IngestionRequest request) {
        ingestionService.ingestData(request);
        return ResponseEntity.ok("Ingestion job queued for source=" + request.getSource() +
                " ticker=" + request.getTicker());
    }
}

