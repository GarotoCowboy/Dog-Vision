package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    @Query("""
           select e
           from Employee e
           join fetch e.user u
           """)
    List<Employee> findAllWithUser();

    @Query("""
           select e
           from Employee e
           join fetch e.user u
           where e.id = :id
           """)
    Optional<Employee> findByIdWithUser(UUID id);

}
