package br.com.dogvision.user.service;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.response.TrainerResponse;

import java.util.List;
import java.util.UUID;

public interface TrainerService {

    TrainerResponse getById(UUID id);

    List<TrainerResponse> getAll();

    TrainerResponse getByRegistration(String registration);

    TrainerResponse save(CreateTrainerRequest dto);

    void delete(UUID id);
}
