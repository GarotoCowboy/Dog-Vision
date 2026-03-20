package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "veterinarians")
public class Veterinarian {

    @Id
    @Column(name = "employee_id")
    private UUID id; // mesmo ID do Employee

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotBlank @NotNull
    @Column(nullable = false, unique = true)
    private String crmv;

    // Se "areaOfExpertise" for mesmo do Vet, mantém.
    // Se não, remove e deixa só em Trainer.
    @NotBlank @NotNull
    @Column(nullable = false)
    private String areaOfExpertise;
}
