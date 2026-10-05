package br.com.dogvision.dogtraining.repository;

import br.com.dogvision.dogtraining.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, UUID> {

    List<Training> findAllByDogId(UUID dogId);

    List<Training> findAllByTrainerId(UUID trainerId);
}