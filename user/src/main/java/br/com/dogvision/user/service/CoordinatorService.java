package br.com.dogvision.user.service;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.create.CreateFirstCoordinatorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;

import java.util.List;
import java.util.UUID;

public interface CoordinatorService {

    CoordinatorResponse getById(UUID id);

    List<CoordinatorResponse> getAll();

    CoordinatorResponse getByRegistration(String registration);

    CoordinatorResponse save(CreateCoordinatorRequest dto);

    CoordinatorResponse createFirstCoordinator(CreateFirstCoordinatorRequest dto);

    void delete(UUID id);
}
