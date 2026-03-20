package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DogWeightRepository extends JpaRepository<DogWeight, UUID> {

    List<DogWeight> findAllByDogId(UUID dogId);

    List<DogWeight> findAllByDogIdAndCreatedAtBetween(UUID dogId, LocalDateTime startsAt, LocalDateTime endsAt );

    Optional<DogWeight> findTopByDogIdOrderByCreatedAtDesc(UUID dogId);
}
