package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogBirthRequest;

import java.util.List;
import java.util.UUID;

public interface DogsBirthService {

    DogBirthResponse getById(UUID id);

    List<DogBirthResponse> getAll();

    DogBirthResponse save(CreateDogBirthRequest dto, UUID veterinarianId);

    DogBirthResponse update(UUID id, UpdateDogBirthRequest dto);

    void delete(UUID id);
}
