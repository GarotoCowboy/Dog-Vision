package br.com.dogvision.dogtraining.service.imp;

import br.com.dogvision.dogtraining.dto.create.CreateStage3Request;
import br.com.dogvision.dogtraining.dto.mapper.Stage3Mapper;
import br.com.dogvision.dogtraining.dto.response.Stage3Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage3Request;
import br.com.dogvision.dogtraining.infra.exception.BusinessException;
import br.com.dogvision.dogtraining.infra.exception.ResourceNotFoundException;
import br.com.dogvision.dogtraining.model.Stage3;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.repository.Stage3Repository;
import br.com.dogvision.dogtraining.service.Stage3Service;
import br.com.dogvision.dogtraining.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Stage3ServiceImp implements Stage3Service {

    private final Stage3Repository repository;
    private final Stage3Mapper mapper;
    private final TrainingService trainingService;

    @Override
    @Transactional
    public Stage3Response save(CreateStage3Request dto) {
        long count = repository.countByTrainingId(dto.trainingId());
        if (count >= 21) {
            throw new BusinessException("A etapa 3 já atingiu o limite máximo de 21 dias registrados.", HttpStatus.BAD_REQUEST);
        }

        Stage3 stage = mapper.toEntity(dto);
        Stage3 saved = repository.save(stage);
        return mapper.toResponse(saved);
    }

    @Override
    public Stage3Response getById(UUID id) {
        Stage3 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage3", id));
        return mapper.toResponse(stage);
    }

    @Override
    public List<Stage3Response> findAllByTrainingId(UUID trainingId) {
        return repository.findAllByTrainingId(trainingId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public Stage3Response update(UUID id, UpdateStage3Request dto) {
        Stage3 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage3", id));

        mapper.updateFromDto(dto, stage);
        repository.save(stage);
        return mapper.toResponse(stage);
    }

    @Override
    @Transactional
    public Stage3Response finalizeStage(UUID id, TraningStatus status) {
        Stage3 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage3", id));

        stage.setStatus(status);
        repository.save(stage);

        if (status == TraningStatus.Apto) {
            trainingService.advanceStage(stage.getTrainingId());
        }

        return mapper.toResponse(stage);
    }

    @Override
    public void delete(UUID id) {
        Stage3 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage3", id));
        repository.delete(stage);
    }
}