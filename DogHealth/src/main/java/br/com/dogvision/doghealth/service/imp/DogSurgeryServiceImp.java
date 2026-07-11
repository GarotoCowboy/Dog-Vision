package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogSurgeryRequest;
import br.com.dogvision.doghealth.dto.mapper.DogSurgeryMapper;
import br.com.dogvision.doghealth.dto.response.DogSurgeryResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogSurgeryRequest;
import br.com.dogvision.doghealth.infra.exception.SurgeryNotFoundException;
import br.com.dogvision.doghealth.model.DogSurgery;
import br.com.dogvision.doghealth.repository.DogSurgeryRepository;
import br.com.dogvision.doghealth.service.DogSurgeryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DogSurgeryServiceImp implements DogSurgeryService {

    private final DogSurgeryRepository repository;
    private final DogSurgeryMapper mapper;


    @Override
    public DogSurgeryResponse getById(UUID id) {
        DogSurgery surgery = repository.findById(id)
                .orElseThrow(() -> new SurgeryNotFoundException(id));

        return mapper.toResponse(surgery);
    }

    @Override
    public List<DogSurgeryResponse> findAllByDogId(UUID dogId) {
        return repository.findAllByDogId(dogId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Page<DogSurgeryResponse> findByDogIdAndDateTimeOfSurgeryBetween(UUID dogId, LocalDateTime startDateTimeOfSurgery,
                                                                           LocalDateTime endDateTimeOfSurgery, int pages,int size) {

        Pageable pageable = PageRequest.of(pages,size, Sort.by("dateTimeOfSurgery").ascending());

        return repository.findByDogIdAndDateTimeOfSurgeryBetween(dogId,startDateTimeOfSurgery,endDateTimeOfSurgery,pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<DogSurgeryResponse> findByDateTimeOfSurgeryBetween(LocalDateTime startDateTimeOfSurgery,
                                                                   LocalDateTime endDateTimeOfSurgery,int pages,int size) {

        Pageable pageable = PageRequest.of(pages,size,Sort.by("dateTimeOfSurgery").ascending());

        return repository.findByDateTimeOfSurgeryBetween(startDateTimeOfSurgery,endDateTimeOfSurgery,pageable)
                .map(mapper::toResponse);
    }

    @Override
    public DogSurgeryResponse save(CreateDogSurgeryRequest dto, UUID veterinarianId) {
        DogSurgery dogSurgery = mapper.toEntity(dto);
        dogSurgery.setVeterinarianId(veterinarianId);
        DogSurgery savedSurgery = repository.save(dogSurgery);
        return mapper.toResponse(savedSurgery);
    }

    @Override
    public DogSurgeryResponse update(UUID id, UpdateDogSurgeryRequest dto) {
        DogSurgery surgery = repository.findById(id)
                .orElseThrow(() -> new SurgeryNotFoundException(id));

        mapper.updateFromDto(dto,surgery);
        repository.save(surgery);

        return mapper.toResponse(surgery);
    }

    @Override
    public void delete(UUID id) {
        DogSurgery surgery = repository.findById(id)
                .orElseThrow(() -> new SurgeryNotFoundException(id));

        repository.delete(surgery);
    }
}
