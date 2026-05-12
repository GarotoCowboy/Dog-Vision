package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.mapper.ConsultationMapper;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.infra.exception.ConsultationNotFoundException;
import br.com.dogvision.doghealth.model.Consultation;
import br.com.dogvision.doghealth.repository.ConsultationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceImpTest {

    @Mock
    private ConsultationRepository repository;

    @Mock
    private ConsultationMapper mapper;

    private ConsultationServiceImp service;

    @BeforeEach
    void setUp() {
        service = new ConsultationServiceImp(repository, mapper);
    }

    @Test
    void shouldReturnConsultationById() {
        Consultation consultation = consultation();
        ConsultationResponse response = consultationResponse();

        when(repository.findById(consultation.getId())).thenReturn(Optional.of(consultation));
        when(mapper.toResponse(consultation)).thenReturn(response);

        ConsultationResponse result = service.getById(consultation.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldFilterConsultationsByMonth() {
        UUID dogId = UUID.randomUUID();
        Consultation consultation = consultation();
        ConsultationResponse response = consultationResponse();
        LocalDateTime expectedStart = LocalDateTime.of(2026, 4, 1, 0, 0, 0);
        LocalDateTime expectedEnd = LocalDateTime.of(2026, 4, 30, 23, 59, 59);

        when(repository.findAllByDogIdAndCreatedAtBetween(dogId, expectedStart, expectedEnd))
                .thenReturn(List.of(consultation));
        when(mapper.toResponse(consultation)).thenReturn(response);

        List<ConsultationResponse> result = service.getByMonth(dogId, 4, 2026);

        assertThat(result).containsExactly(response);
    }

    @Test
    void shouldSaveConsultationWithVeterinarianId() {
        UUID veterinarianId = UUID.randomUUID();
        CreateConsultationRequest request = new CreateConsultationRequest(
                UUID.randomUUID(),
                "Antibiotic",
                "Thor",
                "Golden",
                "Dermatitis",
                LocalDate.now()
        );
        Consultation consultation = consultation();
        Consultation saved = consultation();
        ConsultationResponse response = consultationResponse();

        when(mapper.toEntity(request)).thenReturn(consultation);
        when(repository.save(consultation)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        ConsultationResponse result = service.save(request, veterinarianId);

        assertThat(result).isEqualTo(response);
        assertThat(consultation.getVeterinarianId()).isEqualTo(veterinarianId);
    }

    @Test
    void shouldUpdateConsultation() {
        Consultation consultation = consultation();
        ConsultationResponse response = consultationResponse();
        UpdateConsultationRequest request = new UpdateConsultationRequest("Rest", "Healthy");

        when(repository.findById(consultation.getId())).thenReturn(Optional.of(consultation));
        when(mapper.toResponse(consultation)).thenReturn(response);

        ConsultationResponse result = service.update(consultation.getId(), request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateFromDto(request, consultation);
        verify(repository).save(consultation);
    }

    @Test
    void shouldDeleteConsultation() {
        Consultation consultation = consultation();

        when(repository.findById(consultation.getId())).thenReturn(Optional.of(consultation));

        service.delete(consultation.getId());

        verify(repository).delete(consultation);
    }

    @Test
    void shouldThrowWhenConsultationDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ConsultationNotFoundException.class);
    }

    private Consultation consultation() {
        Consultation consultation = new Consultation();
        consultation.setId(UUID.randomUUID());
        consultation.setDogId(UUID.randomUUID());
        consultation.setVeterinarianId(UUID.randomUUID());
        consultation.setDogsName("Thor");
        consultation.setDogsBreed("Golden");
        consultation.setTreatment("Antibiotic");
        consultation.setDiagnosis("Dermatitis");
        consultation.setCreatedAt(LocalDateTime.now());
        consultation.setUpdatedAt(LocalDateTime.now());
        return consultation;
    }

    private ConsultationResponse consultationResponse() {
        return new ConsultationResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Thor",
                "Golden",
                "Antibiotic",
                "Dermatitis",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
