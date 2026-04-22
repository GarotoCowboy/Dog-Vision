package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.Consultation;
import br.com.dogvision.doghealth.model.DogWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    List<Consultation> findAllByDogIdAndCreatedAtBetween(UUID dogId, LocalDateTime startsAt, LocalDateTime endsAt);

}
