package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "coordinators")
@EqualsAndHashCode(callSuper = true)
public class Coordinator extends Employee {

    @NotNull
    @Column(nullable = false,unique = true)
    private Integer codAdmin;

    public Coordinator(Employee employee){
        super(employee);
    }
}
