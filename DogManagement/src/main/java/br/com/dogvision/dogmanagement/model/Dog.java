package br.com.dogvision.dogmanagement.model;

import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.Times;
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
@AllArgsConstructor
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;

    private String name;

    private DogRace race;

    @Enumerated(EnumType.STRING)
    private DogStatus status;

    private Character sex;

    private Timestamp dateOfBirth;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @CreatedDate
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Column
    private Timestamp updatedAt;
}
