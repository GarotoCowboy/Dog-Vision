package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;

import java.util.List;
import java.util.UUID;

public interface ConsultationService {

    ConsultationResponse getById(UUID id);

    List<ConsultationResponse> getAll();

    ConsultationResponse save(CreateConsultationRequest dto,UUID veterinarianId);

    ConsultationResponse update(UUID id, UpdateConsultationRequest dto);

    void delete(UUID id);
}
