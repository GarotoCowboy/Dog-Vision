package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogReproductionRequest;
import br.com.dogvision.doghealth.dto.mapper.DogReproductionMapper;
import br.com.dogvision.doghealth.dto.response.DogReproductionResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogReproductionRequest;
import br.com.dogvision.doghealth.infra.exception.ReproductionNotFoundException;
import br.com.dogvision.doghealth.model.DogReproduction;
import br.com.dogvision.doghealth.repository.DogReproductionRepository;
import br.com.dogvision.doghealth.service.DogReproductionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DogReproductionServiceImp implements DogReproductionService {

    private static final int DEFAULT_CYCLE_INTERVAL_MONTHS = 6;

    private final DogReproductionRepository repository;
    private final DogReproductionMapper mapper;

    @Override
    public DogReproductionResponse save(CreateDogReproductionRequest dto, UUID veterinarianId) {
        DogReproduction entity = mapper.toEntity(dto);
        entity.setVeterinarianId(veterinarianId);

        int interval = (dto.cycleIntervalInMonths() != null && dto.cycleIntervalInMonths() > 0)
                ? dto.cycleIntervalInMonths()
                : DEFAULT_CYCLE_INTERVAL_MONTHS;
        entity.setCycleIntervalInMonths(interval);

        if (dto.expectedNextHeatDate() != null) {
            entity.setExpectedNextHeatDate(dto.expectedNextHeatDate());
        } else {
            entity.setExpectedNextHeatDate(dto.date().plusMonths(interval));
        }

        DogReproduction saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public DogReproductionResponse getById(UUID id) {
        DogReproduction entity = repository.findById(id)
                .orElseThrow(() -> new ReproductionNotFoundException(id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<DogReproductionResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<DogReproductionResponse> listByDogId(UUID dogId) {
        return repository.findAllByDogIdOrderByDateDesc(dogId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Optional<DogReproductionResponse> getLastHeatByDogId(UUID dogId) {
        return repository.findTopByDogIdOrderByDateDesc(dogId)
                .map(mapper::toResponse);
    }

    @Override
    public DogReproductionResponse update(UUID id, UpdateDogReproductionRequest dto) {
        DogReproduction entity = repository.findById(id)
                .orElseThrow(() -> new ReproductionNotFoundException(id));

        mapper.updateFromDto(dto, entity);

        int interval = entity.getCycleIntervalInMonths() != null && entity.getCycleIntervalInMonths() > 0
                ? entity.getCycleIntervalInMonths()
                : DEFAULT_CYCLE_INTERVAL_MONTHS;
        entity.setCycleIntervalInMonths(interval);

        if (dto.date() != null && dto.expectedNextHeatDate() == null) {
            entity.setExpectedNextHeatDate(dto.date().plusMonths(interval));
        }

        DogReproduction updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(UUID id) {
        DogReproduction entity = repository.findById(id)
                .orElseThrow(() -> new ReproductionNotFoundException(id));
        repository.delete(entity);
    }
}
