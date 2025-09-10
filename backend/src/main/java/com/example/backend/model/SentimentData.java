package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sentiment_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentimentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;
    private String source;
    private Double sentimentScore;
    private String sentimentLabel;

    private LocalDateTime collectedAt = LocalDateTime.now();
}
