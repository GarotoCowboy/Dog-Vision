package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "coordinators")
@EqualsAndHashCode(callSuper = true)
public class Coordinator extends Employee {

//    @Id
//    @Column(name = "employee_id")
//    private UUID id;

//    @MapsId
//    @OneToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id", nullable = false)
//    private Employee employee;

    @NotNull
    @Column(nullable = false,unique = true)
    private Integer codAdmin;
}
