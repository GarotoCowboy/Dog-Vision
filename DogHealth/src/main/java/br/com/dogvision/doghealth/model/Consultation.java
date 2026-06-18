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
@Getter
@Setter
@NoArgsConstructor
public class Consultation {

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
    //-------------------------------------

    @Column(nullable = false)
    private String treatment;

    @Column(nullable = false)
    private String diagnosis;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
