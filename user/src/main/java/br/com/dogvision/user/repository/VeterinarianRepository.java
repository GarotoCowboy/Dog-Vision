package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, UUID> {

    boolean existsByCrmv(String crmv);

    @Query("""
           select v
           from Veterinarian v
           join fetch v.user u
           """)
    List<Veterinarian> findAllWithUser();

    @Query("""
           select v
           from Veterinarian v
           join fetch v.user u
           where v.id = :id
           """)
    Optional<Veterinarian> findByIdWithUser(UUID id);

    @Query("""
            select v
            from Veterinarian v
            join fetch v.user u
            where u.registration = :registration
            """)
    Optional<Veterinarian> findByRegistration(@Param("registration") String registration);
}
