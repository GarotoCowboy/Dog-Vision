package br.com.dogvision.dogtraining.model;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID trainerId;

    @Column(nullable = false)
    private UUID dogId;

    // DOG'S SNAPSHOT
    @Column(nullable = false)
    private String dogsName;

    @Column(nullable = false)
    private String dogsBreed;

    @NotNull(message = "The training date is mandatory")
    private int monthYear;

    @NotNull(message = "The training day is mandatory")
    private int day;

    @NotNull(message = "The evaluation type is mandatory")
    private String evaluationType;

    // --- NOVOS CAMPOS ADICIONADOS ---
    @Column(nullable = false)
    private int currentStage = 1; // Inicia por padrão na etapa 1

    @Enumerated(EnumType.STRING)
    private TraningStatus status; // Definido como Apto/Inapto ao finalizar a etapa 4
}