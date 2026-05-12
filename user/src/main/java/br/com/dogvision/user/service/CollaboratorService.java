package br.com.dogvision.user.service;

import br.com.dogvision.user.dto.create.CreateCollaboratorRequest;
import br.com.dogvision.user.dto.response.CollaboratorResponse;

import java.util.List;
import java.util.UUID;

public interface CollaboratorService {

    CollaboratorResponse getById(UUID id);

    CollaboratorResponse getByRegistration(String registration);

    List<CollaboratorResponse> getAll();

    CollaboratorResponse save(CreateCollaboratorRequest dto);

    void delete(UUID id);
}
