package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "collaborators")
@Inheritance(strategy = InheritanceType.JOINED)
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShiftEnum shift;

    public Collaborator(Collaborator collaborator) {
        this.id = collaborator.id;
        this.user = collaborator.user;
        this.name = collaborator.name;
        this.shift = collaborator.shift;
    }
}
