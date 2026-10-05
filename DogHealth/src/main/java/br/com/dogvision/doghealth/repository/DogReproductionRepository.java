package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogReproduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DogReproductionRepository extends JpaRepository<DogReproduction, UUID> {

    List<DogReproduction> findAllByDogIdOrderByDateDesc(UUID dogId);

    Optional<DogReproduction> findTopByDogIdOrderByDateDesc(UUID dogId);
}
