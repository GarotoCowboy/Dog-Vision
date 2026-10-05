package br.com.dogvision.dogtraining.service;

import br.com.dogvision.dogtraining.dto.create.CreateTrainingRequest;
import br.com.dogvision.dogtraining.dto.response.TrainingResponse;
import br.com.dogvision.dogtraining.dto.update.UpdateTrainingRequest;

import java.util.List;
import java.util.UUID;

public interface TrainingService {

    TrainingResponse save(CreateTrainingRequest dto);

    TrainingResponse getById(UUID id);

    List<TrainingResponse> findAllByDogId(UUID dogId);

    List<TrainingResponse> findAllByTrainerId(UUID trainerId);

    TrainingResponse update(UUID id, UpdateTrainingRequest dto);

    TrainingResponse advanceStage(UUID id); // <-- Método para avançar a etapa

    void delete(UUID id);
}