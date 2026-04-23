package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "veterinarians")
@EqualsAndHashCode(callSuper = true)
public class Veterinarian extends Employee {

    @NotBlank @NotNull
    @Column(nullable = false, unique = true)
    private String crmv;

    @NotBlank @NotNull
    @Column(nullable = false)
    private String areaOfExpertise;

    public Veterinarian(Employee employee) {
        super(employee);
    }
}
