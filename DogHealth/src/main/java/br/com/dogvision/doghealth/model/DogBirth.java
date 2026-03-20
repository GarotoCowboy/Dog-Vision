package br.com.dogvision.doghealth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DogBirth {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID dogId;

    //DOG`S SNAPSHOT
    @Column(nullable = false)
    private String dogsName;

    @Column(nullable = false)
    private String dogsBreed;
 //--------------------------------

    private UUID veterinarianId;

    private LocalDateTime date;

    private int numberOfPuppies;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
