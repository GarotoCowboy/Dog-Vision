package br.com.dogvision.dogtraining.repository;

import br.com.dogvision.dogtraining.model.Stage1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Stage1Repository extends JpaRepository<Stage1, UUID> {

    List<Stage1> findAllByTrainingId(UUID trainingId);

    Optional<Stage1> findByTrainingId(UUID trainingId);

    long countByTrainingId(UUID trainingId);
}