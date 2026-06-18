package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "trainers")
@EqualsAndHashCode(callSuper = true)
public class Trainer extends Employee {

    @Column(nullable = false)
    private String areaOfExpertise;


}
