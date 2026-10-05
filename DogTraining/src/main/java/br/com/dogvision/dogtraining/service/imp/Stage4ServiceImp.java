package br.com.dogvision.dogtraining.service.imp;

import br.com.dogvision.dogtraining.dto.create.CreateStage4Request;
import br.com.dogvision.dogtraining.dto.mapper.Stage4Mapper;
import br.com.dogvision.dogtraining.dto.response.Stage4Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage4Request;
import br.com.dogvision.dogtraining.infra.exception.BusinessException;
import br.com.dogvision.dogtraining.infra.exception.ResourceNotFoundException;
import br.com.dogvision.dogtraining.model.Stage4;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.repository.Stage4Repository;
import br.com.dogvision.dogtraining.service.Stage4Service;
import br.com.dogvision.dogtraining.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Stage4ServiceImp implements Stage4Service {

    private final Stage4Repository repository;
    private final Stage4Mapper mapper;
    private final TrainingService trainingService;

    @Override
    @Transactional
    public Stage4Response save(CreateStage4Request dto) {
        long count = repository.countByTrainingId(dto.trainingId());
        if (count >= 21) {
            throw new BusinessException("A etapa 4 já atingiu o limite máximo de 21 dias registrados.", HttpStatus.BAD_REQUEST);
        }

        Stage4 stage = mapper.toEntity(dto);
        Stage4 saved = repository.save(stage);
        return mapper.toResponse(saved);
    }

    @Override
    public Stage4Response getById(UUID id) {
        Stage4 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage4", id));
        return mapper.toResponse(stage);
    }

    @Override
    public List<Stage4Response> findAllByTrainingId(UUID trainingId) {
        return repository.findAllByTrainingId(trainingId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public Stage4Response update(UUID id, UpdateStage4Request dto) {
        Stage4 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage4", id));

        mapper.updateFromDto(dto, stage);
        repository.save(stage);
        return mapper.toResponse(stage);
    }

    @Override
    @Transactional
    public Stage4Response finalizeStage(UUID id, TraningStatus status) {
        Stage4 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage4", id));

        stage.setStatus(status);
        repository.save(stage);

        if (status == TraningStatus.Apto) {
            trainingService.advanceStage(stage.getTrainingId());
        }

        return mapper.toResponse(stage);
    }

    @Override
    public void delete(UUID id) {
        Stage4 stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage4", id));
        repository.delete(stage);
    }
}