package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogSurgery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DogSurgeryRepository extends JpaRepository<DogSurgery,UUID> {

    List<DogSurgery> findAllByDogId(UUID dogId);

    Page<DogSurgery> findByDogIdAndDateTimeOfSurgeryBetween(UUID dogId, LocalDateTime startDateTimeOfSurgery, LocalDateTime endDateTimeOfSurgery, Pageable pageable);
    Page<DogSurgery> findByDateTimeOfSurgeryBetween(LocalDateTime startDateTimeOfSurgery, LocalDateTime endDateTimeOfSurgery, Pageable pageable);
}
