package br.com.dogvision.dogtraining.repository;

import br.com.dogvision.dogtraining.model.Stage2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Stage2Repository extends JpaRepository<Stage2, UUID> {

    List<Stage2> findAllByTrainingId(UUID trainingId);

    Optional<Stage2> findByTrainingId(UUID trainingId);

    long countByTrainingId(UUID trainingId);
}