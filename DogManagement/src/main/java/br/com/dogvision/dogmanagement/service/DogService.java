package br.com.dogvision.dogmanagement.service;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.infra.exceptions.DogNotFoundException;
import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.repository.DogRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DogService {

    DogResponse getById(UUID id);
    List<DogResponse> getAll();
    DogResponse save(CreateDogRequest dto);
    DogResponse update(UUID id, UpdateDogRequest updateDogRequest);
    void delete(UUID id);
}
