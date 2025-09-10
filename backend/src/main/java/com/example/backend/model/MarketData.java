package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "market_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ticker", "date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;
    private LocalDate date;

    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
}
