package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateDogMassageRequest;
import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.response.DogMassageResponse;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogMassageRequest;
import br.com.dogvision.doghealth.dto.update.UpdateDogWeightRequest;
import br.com.dogvision.doghealth.model.DogMassage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DogMassageService {

    List<DogMassageResponse> listByDogId(UUID id);

    List<DogMassageResponse> getByMonth(UUID id,int month, int year);

    List<DogMassageResponse> getByWeek(UUID id, LocalDate date);

    Optional<DogMassageResponse> getLastMassage(UUID id);

    DogMassageResponse save(CreateDogMassageRequest dto, UUID collaboratorId);

    DogMassageResponse update(UUID id, UpdateDogMassageRequest dto);

    void delete(UUID id);

}
