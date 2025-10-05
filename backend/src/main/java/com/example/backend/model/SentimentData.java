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
    @Column(name = "sentiment_score")
    private Double sentimentScore;
    @Column(name = "sentiment_label")
    private String sentimentLabel;
    private String url;

    private LocalDateTime date = LocalDateTime.now();
}
