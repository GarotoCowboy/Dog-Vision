package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollaboratorRepository extends JpaRepository<Collaborator, UUID> {

    @Query("""
           select m
           from Collaborator m
           join fetch m.user u
           """)
    List<Collaborator> findAllWithUser();

    @Query("""
           select m
           from Collaborator m
           join fetch m.user u
           where m.id = :id
           """)
    Optional<Collaborator> findByIdWithUser(UUID id);

    @Query("""
            select m 
            from Collaborator m 
            join fetch m.user u
            where u.registration = :registration            
            """)
    Optional<Collaborator> findByRegistration(@Param("registration") String registration);
}
