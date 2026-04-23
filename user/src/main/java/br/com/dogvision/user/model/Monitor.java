package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "monitors")
@Inheritance(strategy = InheritanceType.JOINED)
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotNull @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShiftEnum shift;

    public Monitor(Monitor monitor) {
        this.id = monitor.id;
        this.user = monitor.user;
        this.shift = monitor.shift;
    }
}
