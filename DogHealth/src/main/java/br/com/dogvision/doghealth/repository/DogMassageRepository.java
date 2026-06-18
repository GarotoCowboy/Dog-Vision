package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogBirth;
import br.com.dogvision.doghealth.model.DogMassage;
import br.com.dogvision.doghealth.model.DogWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DogMassageRepository extends JpaRepository<DogMassage, UUID> {

    List<DogMassage> findAllByDogId(UUID dogId);

    List<DogMassage> findAllByDogIdAndCreatedAtBetween(UUID dogId, LocalDateTime startsAt, LocalDateTime endsAt );

    Optional<DogMassage> findTopByDogIdOrderByCreatedAtDesc(UUID dogId);
}
