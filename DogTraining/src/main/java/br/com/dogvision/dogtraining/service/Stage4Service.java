package br.com.dogvision.dogtraining.service;

import br.com.dogvision.dogtraining.dto.create.CreateStage4Request;
import br.com.dogvision.dogtraining.dto.response.Stage4Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage4Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.List;
import java.util.UUID;

public interface Stage4Service {
    Stage4Response save(CreateStage4Request dto);
    Stage4Response getById(UUID id);
    List<Stage4Response> findAllByTrainingId(UUID trainingId);
    Stage4Response update(UUID id, UpdateStage4Request dto);
    Stage4Response finalizeStage(UUID id, TraningStatus status);
    void delete(UUID id);
}