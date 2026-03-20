package br.com.dogvision.user.service;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.response.VeterinarianResponse;

import java.util.List;
import java.util.UUID;

public interface VeterinarianService {

    VeterinarianResponse getById(UUID id);

    List<VeterinarianResponse> getAll();

    VeterinarianResponse getByRegistration(String registration);

    VeterinarianResponse save(CreateVeterinarianRequest dto);

    void delete(UUID id);
}
