package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogWeightRequest;
import br.com.dogvision.doghealth.model.DogWeight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DogWeightService {

    List<DogWeightResponse> listByDogId(UUID id);

    List<DogWeightResponse> getByMonth(UUID id,int month, int year);

    List<DogWeightResponse> getByWeek(UUID id, LocalDate date);

    Optional<DogWeightResponse> getLastWeight(UUID id);

    DogWeightResponse save(CreateDogWeightRequest dto, UUID veterinarianId);

    DogWeightResponse update(UUID id, UpdateDogWeightRequest dto);

    void delete(UUID id);

}
