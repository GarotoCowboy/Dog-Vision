package br.com.dogvision.dogtraining.service.imp;

import br.com.dogvision.dogtraining.dto.create.CreateStage1Request;
import br.com.dogvision.dogtraining.dto.mapper.Stage1Mapper;
import br.com.dogvision.dogtraining.dto.response.Stage1Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage1Request;
import br.com.dogvision.dogtraining.infra.exception.BusinessException;
import br.com.dogvision.dogtraining.infra.exception.ResourceNotFoundException;
import br.com.dogvision.dogtraining.model.Stage1;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.repository.Stage1Repository;
import br.com.dogvision.dogtraining.service.Stage1Service;
import br.com.dogvision.dogtraining.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Stage1ServiceImp implements Stage1Service {

    private final Stage1Repository repository;
    private final Stage1Mapper mapper;
    private final TrainingService trainingService;

    @Override
    @Transactional
    public Stage1Response save(CreateStage1Request dto) {
        long count = repository.countByTrainingId(dto.trainingId());
        if (count >= 21) {
            throw new BusinessException("A etapa 1 já atingiu o limite máximo de 21 dias registrados.", HttpStatus.BAD_REQUEST);
        }

        Stage1 stage = mapper.toEntity(dto);
        Stage1 saved = repository.save(stage);
        return mapper.toResponse(saved);
    }

    @Override
    public Stage1Response getById(UUID id) {
        Stage1 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage1", id));
        return mapper.toResponse(stage);
    }

    @Override
    public List<Stage1Response> findAllByTrainingId(UUID trainingId) {
        return repository.findAllByTrainingId(trainingId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public Stage1Response update(UUID id, UpdateStage1Request dto) {
        Stage1 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage1", id));

        mapper.updateFromDto(dto, stage);
        repository.save(stage);
        return mapper.toResponse(stage);
    }

    @Override
    @Transactional
    public Stage1Response finalizeStage(UUID id, TraningStatus status) {
        Stage1 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage1", id));

        stage.setStatus(status);
        repository.save(stage);

        if (status == TraningStatus.Apto) {
            trainingService.advanceStage(stage.getTrainingId());
        }

        return mapper.toResponse(stage);
    }

    @Override
    public void delete(UUID id) {
        Stage1 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage1", id));
        repository.delete(stage);
    }
}