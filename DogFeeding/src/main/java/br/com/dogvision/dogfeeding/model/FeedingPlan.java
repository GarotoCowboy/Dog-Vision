package br.com.dogvision.dogfeeding.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FeedingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID dogId;

    @Column(nullable = false)
    private UUID rationId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String goal;

    @Column(nullable = false)
    private double dailyQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeasurementUnit unit;

    @ElementCollection(targetClass = MealType.class)
    @CollectionTable(name = "feeding_plan_meal_types", joinColumns = @JoinColumn(name = "feeding_plan_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private List<MealType> mealTypes = new ArrayList<>();

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
