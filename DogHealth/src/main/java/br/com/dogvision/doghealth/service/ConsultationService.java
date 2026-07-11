package br.com.dogvision.doghealth.service;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ConsultationService {

    ConsultationResponse getById(UUID id);

    Page<ConsultationResponse> getByDogId(UUID dogId, int pages, int size);

    Page<ConsultationResponse> getAll(int pages,int size);

    Page<ConsultationResponse> getByMonth(UUID id, int month, int year, int pages,int size);

    ConsultationResponse save(CreateConsultationRequest dto,UUID veterinarianId);

    ConsultationResponse update(UUID id, UpdateConsultationRequest dto);

    void delete(UUID id);
}
