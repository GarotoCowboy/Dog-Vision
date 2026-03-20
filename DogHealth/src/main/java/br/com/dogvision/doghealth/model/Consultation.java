package br.com.dogvision.doghealth.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @Column(nullable = false)
    private LocalDate date;

}
