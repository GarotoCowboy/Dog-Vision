package br.com.dogvision.doghealth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dog_reproduction")
@Getter
@Setter
@NoArgsConstructor
public class DogReproduction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID dogId;

    // DOG'S SNAPSHOT
    @Column(nullable = false)
    private String dogsName;

    @Column(nullable = false)
    private String dogsBreed;

    private UUID veterinarianId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDate expectedNextHeatDate;

    private Integer cycleIntervalInMonths;

    private String observations;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
