package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.mapper.DogWeightMapper;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogWeightRequest;
import br.com.dogvision.doghealth.infra.exception.ResourceNotFoundException;
import br.com.dogvision.doghealth.model.DogBirth;
import br.com.dogvision.doghealth.model.DogWeight;
import br.com.dogvision.doghealth.repository.DogWeightRepository;
import br.com.dogvision.doghealth.service.DogWeightService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DogWeightServiceImp implements DogWeightService {

    private final DogWeightRepository repository;
    private final DogWeightMapper mapper;

    @Override
    public List<DogWeightResponse> listByDogId(UUID id) {
        return repository.findAllByDogId(id)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<DogWeightResponse> getByMonth(UUID id, int month, int year) {

        LocalDateTime startsAt = LocalDateTime.of(year,month,1,0,0,0);
        LocalDateTime endsAt = YearMonth.of(year,month)
                .atEndOfMonth()
                .atTime(23,59,59);

        return repository.findAllByDogIdAndCreatedAtBetween(id,startsAt,endsAt)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<DogWeightResponse> getByWeek(UUID id, LocalDate date) {
        LocalDateTime startsAt = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endsAt = date.with(DayOfWeek.SUNDAY).atStartOfDay();

        return repository.findAllByDogIdAndCreatedAtBetween(id,startsAt,endsAt)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

    @Override
    public Optional<DogWeightResponse> getLastWeight(UUID id) {

        return repository.findTopByDogIdOrderByCreatedAtDesc(id)
                .map(mapper::toResponse);

    }

    @Override
    public DogWeightResponse save(CreateDogWeightRequest dto, UUID monitorId) {
        DogWeight dogWeight = mapper.toEntity(dto);
        dogWeight.setMonitorId(monitorId);

        DogWeight savedDogWeight = repository.save(dogWeight);
        return mapper.toResponse(savedDogWeight);
    }

    @Override
    public DogWeightResponse update(UUID id, UpdateDogWeightRequest dto) {

        DogWeight dogWeight = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DogWeight", id));

        mapper.updateFromDto(dto, dogWeight);

        repository.save(dogWeight);
        return mapper.toResponse(dogWeight);


    }

    @Override
    public void delete(UUID id) {
        DogWeight dogWeight = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Weight not found"));

        repository.delete(dogWeight);
    }
}
