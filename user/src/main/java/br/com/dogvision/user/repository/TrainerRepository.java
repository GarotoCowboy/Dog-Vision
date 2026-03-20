package br.com.dogvision.user.repository;

import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerRepository extends JpaRepository<Trainer, UUID> {

    @Query("""
           select t
           from Trainer t
           join fetch t.employee e
           join fetch e.user u
           """)
    List<Trainer> findAllWithEmployeeAndUser();

    @Query("""
           select t
           from Trainer t
           join fetch t.employee e
           join fetch e.user u
           where t.id = :id
           """)
    Optional<Trainer> findByIdWithEmployeeAndUser(UUID id);

    @Query("""
            select t 
            from Trainer t 
            join fetch t.employee e
            join fetch e.user u
            where u.registration = :registration            
            """)
    Optional<Trainer> findByRegistration(@Param("registration") String registration);
}
