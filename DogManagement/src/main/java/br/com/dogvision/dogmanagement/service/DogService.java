package br.com.dogvision.dogmanagement.service;

import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.model.Dog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DogService {

    Optional<DogResponse> getById(UUID id);
    List<DogResponse> getAll();
    Dog save(Dog dog);
    Dog update(UUID id, UpdateDogRequest updateDogRequest);
    void delete(UUID id);
}
