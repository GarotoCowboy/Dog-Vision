package br.com.dogvision.dogtraining.repository;

import br.com.dogvision.dogtraining.model.Stage3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Stage3Repository extends JpaRepository<Stage3, UUID> {

    List<Stage3> findAllByTrainingId(UUID trainingId);

    Optional<Stage3> findByTrainingId(UUID trainingId);

    long countByTrainingId(UUID trainingId);
}