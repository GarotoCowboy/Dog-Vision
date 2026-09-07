package br.com.dogvision.dogtraining.service;

import br.com.dogvision.dogtraining.dto.create.CreateStage2Request;
import br.com.dogvision.dogtraining.dto.response.Stage2Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage2Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.List;
import java.util.UUID;

public interface Stage2Service {
    Stage2Response save(CreateStage2Request dto);
    Stage2Response getById(UUID id);
    List<Stage2Response> findAllByTrainingId(UUID trainingId);
    Stage2Response update(UUID id, UpdateStage2Request dto);
    Stage2Response finalizeStage(UUID id, TraningStatus status);
    void delete(UUID id);
}