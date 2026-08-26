package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateMedicationRequest;
import br.com.dogvision.doghealth.dto.response.MedicationResponse;

import java.util.List;
import java.util.UUID;

public interface MedicationService {

    MedicationResponse save(CreateMedicationRequest dto);

    List<MedicationResponse> getAll();

    MedicationResponse getById(UUID id);

    void delete(UUID id);
}

