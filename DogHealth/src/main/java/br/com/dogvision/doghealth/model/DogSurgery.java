package br.com.dogvision.doghealth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DogSurgery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID veterinarianId;

    @Column(nullable = false)
    private UUID dogId;



    //DOG`S SNAPSHOT
    @Column(nullable = false)
    private String dogsName;
    @Column(nullable = false)
    private String dogsBreed;



    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime dateTimeOfSurgery;

    @Column(nullable = false)
    private String durationExpected;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumUrgency urgency;

    @Column(nullable = false)
    private boolean onFasting;

    @Column(nullable = false)
    private String observation;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
