package br.com.dogvision.dogtraining.service.imp;

import br.com.dogvision.dogtraining.dto.create.CreateTrainingRequest;
import br.com.dogvision.dogtraining.dto.mapper.TrainingMapper;
import br.com.dogvision.dogtraining.dto.response.TrainingResponse;
import br.com.dogvision.dogtraining.dto.update.UpdateTrainingRequest;
import br.com.dogvision.dogtraining.infra.exception.ResourceNotFoundException;
import br.com.dogvision.dogtraining.model.Training;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.repository.TrainingRepository;
import br.com.dogvision.dogtraining.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TrainingServiceImp implements TrainingService {

    private final TrainingRepository repository;
    private final TrainingMapper mapper;

    @Override
    @Transactional
    public TrainingResponse save(CreateTrainingRequest dto) {
        Training training = mapper.toEntity(dto);
        training.setCurrentStage(1); // Força a inicialização na Etapa 1
        Training saved = repository.save(training);
        return mapper.toResponse(saved);
    }

    @Override
    public TrainingResponse getById(UUID id) {
        Training training = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training", id));
        return mapper.toResponse(training);
    }

    @Override
    public List<TrainingResponse> findAllByDogId(UUID dogId) {
        return repository.findAllByDogId(dogId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<TrainingResponse> findAllByTrainerId(UUID trainerId) {
        return repository.findAllByTrainerId(trainerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TrainingResponse update(UUID id, UpdateTrainingRequest dto) {
        Training training = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training", id));

        mapper.updateFromDto(dto, training);
        repository.save(training);
        return mapper.toResponse(training);
    }

    @Override
    @Transactional
    public TrainingResponse advanceStage(UUID id) {
        Training training = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training", id));

        // Se estiver na etapa 4, o treino é concluído como Apto. Caso contrário, avança a etapa.
        if (training.getCurrentStage() >= 4) {
            training.setStatus(TraningStatus.Apto);
        } else {
            training.setCurrentStage(training.getCurrentStage() + 1);
        }

        repository.save(training);
        return mapper.toResponse(training);
    }

    @Override
    public void delete(UUID id) {
        Training training = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training", id));
        repository.delete(training);
    }
}