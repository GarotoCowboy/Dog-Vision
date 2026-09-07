package br.com.dogvision.dogtraining.repository;

import br.com.dogvision.dogtraining.model.Stage4;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Stage4Repository extends JpaRepository<Stage4, UUID> {

    List<Stage4> findAllByTrainingId(UUID trainingId);

    Optional<Stage4> findByTrainingId(UUID trainingId);

    long countByTrainingId(UUID trainingId);
}