package br.com.dogvision.dogtraining.service.imp;

import br.com.dogvision.dogtraining.dto.create.CreateStage2Request;
import br.com.dogvision.dogtraining.dto.mapper.Stage2Mapper;
import br.com.dogvision.dogtraining.dto.response.Stage2Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage2Request;
import br.com.dogvision.dogtraining.infra.exception.BusinessException;
import br.com.dogvision.dogtraining.infra.exception.ResourceNotFoundException;
import br.com.dogvision.dogtraining.model.Stage2;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.repository.Stage2Repository;
import br.com.dogvision.dogtraining.service.Stage2Service;
import br.com.dogvision.dogtraining.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Stage2ServiceImp implements Stage2Service {

    private final Stage2Repository repository;
    private final Stage2Mapper mapper;
    private final TrainingService trainingService;

    @Override
    @Transactional
    public Stage2Response save(CreateStage2Request dto) {
        long count = repository.countByTrainingId(dto.trainingId());
        if (count >= 21) {
            throw new BusinessException("A etapa 2 já atingiu o limite máximo de 21 dias registrados.", HttpStatus.BAD_REQUEST);
        }

        Stage2 stage = mapper.toEntity(dto);
        Stage2 saved = repository.save(stage);
        return mapper.toResponse(saved);
    }

    @Override
    public Stage2Response getById(UUID id) {
        Stage2 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage2", id));
        return mapper.toResponse(stage);
    }

    @Override
    public List<Stage2Response> findAllByTrainingId(UUID trainingId) {
        return repository.findAllByTrainingId(trainingId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public Stage2Response update(UUID id, UpdateStage2Request dto) {
        Stage2 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage2", id));

        mapper.updateFromDto(dto, stage);
        repository.save(stage);
        return mapper.toResponse(stage);
    }

    @Override
    @Transactional
    public Stage2Response finalizeStage(UUID id, TraningStatus status) {
        Stage2 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage2", id));

        stage.setStatus(status);
        repository.save(stage);

        if (status == TraningStatus.Apto) {
            trainingService.advanceStage(stage.getTrainingId());
        }

        return mapper.toResponse(stage);
    }

    @Override
    public void delete(UUID id) {
        Stage2 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage2", id));
        repository.delete(stage);
    }
}