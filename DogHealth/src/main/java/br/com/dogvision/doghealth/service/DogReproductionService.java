package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateDogReproductionRequest;
import br.com.dogvision.doghealth.dto.response.DogReproductionResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogReproductionRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DogReproductionService {

    DogReproductionResponse save(CreateDogReproductionRequest dto, UUID veterinarianId);

    DogReproductionResponse getById(UUID id);

    List<DogReproductionResponse> getAll();

    List<DogReproductionResponse> listByDogId(UUID dogId);

    Optional<DogReproductionResponse> getLastHeatByDogId(UUID dogId);

    DogReproductionResponse update(UUID id, UpdateDogReproductionRequest dto);

    void delete(UUID id);
}
