package br.com.dogvision.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "coordinators")
@EqualsAndHashCode(callSuper = true)
public class Coordinator extends Employee {


}
