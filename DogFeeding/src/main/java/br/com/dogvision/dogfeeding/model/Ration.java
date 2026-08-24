package br.com.dogvision.dogfeeding.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ration {

    public static final double LOW_STOCK_THRESHOLD = 5.0d;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RationType rationType;

    @Column(nullable = false)
    private double currentRationQuantity;

    @Column(nullable = false)
    private LocalDate registrationDate;

    public RationStockStatus getStockStatus(LocalDate referenceDate) {
        if (currentRationQuantity <= 0) {
            return RationStockStatus.OUT_OF_STOCK;
        }
        if (currentRationQuantity <= LOW_STOCK_THRESHOLD) {
            return RationStockStatus.LOW;
        }
        return RationStockStatus.HEALTHY;
    }
}
