package br.com.dogvision.user.repository;

import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.model.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MonitorRepository extends JpaRepository<Monitor, UUID> {

    @Query("""
           select m
           from Monitor m
           join fetch m.employee e
           join fetch e.user u
           """)
    List<Monitor> findAllWithEmployeeAndUser();

    @Query("""
           select m
           from Monitor m
           join fetch m.employee e
           join fetch e.user u
           where m.id = :id
           """)
    Optional<Monitor> findByIdWithEmployeeAndUser(UUID id);

    @Query("""
            select m 
            from Monitor m 
            join fetch m.employee e
            join fetch e.user u
            where u.registration = :registration            
            """)
    Optional<Monitor> findByRegistration(@Param("registration") String registration);
}
