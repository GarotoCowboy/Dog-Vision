package br.com.dogvision.dogtraining.service;

import br.com.dogvision.dogtraining.dto.create.CreateStage1Request;
import br.com.dogvision.dogtraining.dto.response.Stage1Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage1Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.List;
import java.util.UUID;

public interface Stage1Service {
    Stage1Response save(CreateStage1Request dto);
    Stage1Response getById(UUID id);
    List<Stage1Response> findAllByTrainingId(UUID trainingId);
    Stage1Response update(UUID id, UpdateStage1Request dto);
    Stage1Response finalizeStage(UUID id, TraningStatus status);
    void delete(UUID id);
}