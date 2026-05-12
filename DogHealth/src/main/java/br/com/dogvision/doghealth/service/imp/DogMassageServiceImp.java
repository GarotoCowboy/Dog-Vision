package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogMassageRequest;
import br.com.dogvision.doghealth.dto.mapper.DogMassageMapper;
import br.com.dogvision.doghealth.dto.response.DogMassageResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogMassageRequest;
import br.com.dogvision.doghealth.infra.exception.MassageNotFoundException;
import br.com.dogvision.doghealth.model.DogMassage;
import br.com.dogvision.doghealth.repository.DogMassageRepository;
import br.com.dogvision.doghealth.service.DogMassageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DogMassageServiceImp implements DogMassageService  {

    private final DogMassageRepository repository;
    private final DogMassageMapper mapper;


    @Override
    public List<DogMassageResponse> listByDogId(UUID id) {
        return repository.findAllByDogId(id)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<DogMassageResponse> getByMonth(UUID id, int month, int year) {

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
    public List<DogMassageResponse> getByWeek(UUID id, LocalDate date) {
        LocalDateTime startsAt = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endsAt = date.with(DayOfWeek.SUNDAY).atStartOfDay();

        return repository.findAllByDogIdAndCreatedAtBetween(id,startsAt,endsAt)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Optional<DogMassageResponse> getLastMassage(UUID id) {
        return repository.findTopByDogIdOrderByCreatedAtDesc(id)
                .map(mapper::toResponse);
    }

    @Override
    public DogMassageResponse save(CreateDogMassageRequest dto, UUID collaboratorId) {
        DogMassage dogMassage = mapper.toEntity(dto);
        dogMassage.setCollaboratorId(collaboratorId);

        DogMassage savedDogMassage = repository.save(dogMassage);
        return mapper.toResponse(savedDogMassage);
    }

    @Override
    public DogMassageResponse update(UUID id, UpdateDogMassageRequest dto) {
        DogMassage dogMassage = repository.findById(id)
                .orElseThrow(() -> new MassageNotFoundException(id));

        mapper.updateFromDto(dto, dogMassage);

        repository.save(dogMassage);
        return mapper.toResponse(dogMassage);
    }

    @Override
    public void delete(UUID id) {
        DogMassage dogMassage = repository.findById(id)
                .orElseThrow(() -> new MassageNotFoundException(id));

        repository.delete(dogMassage);
    }
}
