package br.com.dogvision.dogmanagement.model;

import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;

    @NotNull
    @NotBlank(message="the dogs name is mandatory")
    private String name;

    @NotNull(message = "the dogs race is mandatory")
    @Enumerated(EnumType.STRING)
    private DogRace race;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "the dogs status is mandatory")
    private DogStatus status;

    @NotNull(message = "the dog sex is mandatory")
    private Character sex;

    @Min(value = 0, message = "the dogs age cannot be negative")
    private int age;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Column(updatable = false)
    @CreatedDate
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Column
    private Timestamp updatedAt;
}
