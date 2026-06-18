package br.com.dogvision.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "employees")
@EntityListeners(AuditingEntityListener.class)
public class Employee extends Collaborator {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime updatedAt;



    public Employee(Employee employee) {
        super(employee);
        this.email = employee.email;
        this.phone = employee.phone;
        this.type = employee.type;
        this.createdAt = employee.createdAt;
        this.updatedAt = employee.updatedAt;
    }
}
