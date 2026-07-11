package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateDogSurgeryRequest;
import br.com.dogvision.doghealth.dto.response.DogSurgeryResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogSurgeryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DogSurgeryService {

    DogSurgeryResponse getById(UUID id);
    List<DogSurgeryResponse> findAllByDogId(UUID dogId);
    Page<DogSurgeryResponse> findByDogIdAndDateTimeOfSurgeryBetween(UUID dogId, LocalDateTime startDateTimeOfSurgery, LocalDateTime endDateTimeOfSurgery, int pages,int size);
    Page<DogSurgeryResponse> findByDateTimeOfSurgeryBetween(LocalDateTime startDateTimeOfSurgery, LocalDateTime endDateTimeOfSurgery, int pages,int size);

    DogSurgeryResponse save(CreateDogSurgeryRequest dto, UUID veterinarianId);

    DogSurgeryResponse update(UUID id, UpdateDogSurgeryRequest dto);
    void delete(UUID id);
}
