package br.com.dogvision.dogtraining.service;

import br.com.dogvision.dogtraining.dto.create.CreateStage3Request;
import br.com.dogvision.dogtraining.dto.response.Stage3Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage3Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.List;
import java.util.UUID;

public interface Stage3Service {
    Stage3Response save(CreateStage3Request dto);
    Stage3Response getById(UUID id);
    List<Stage3Response> findAllByTrainingId(UUID trainingId);
    Stage3Response update(UUID id, UpdateStage3Request dto);
    Stage3Response finalizeStage(UUID id, TraningStatus status);
    void delete(UUID id);
}