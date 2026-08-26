package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateMedicationRequest;
import br.com.dogvision.doghealth.dto.mapper.MedicationMapper;
import br.com.dogvision.doghealth.dto.response.MedicationResponse;
import br.com.dogvision.doghealth.infra.exception.MedicationNotFoundException;
import br.com.dogvision.doghealth.model.Medication;
import br.com.dogvision.doghealth.repository.MedicationRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationServiceImpTest {

    @Mock
    private MedicationRepository repository;

    @Mock
    private MedicationMapper mapper;

    private MedicationServiceImp service;

    @BeforeEach
    void setUp() {
        service = new MedicationServiceImp(repository, mapper);
    }

    @Test
    void shouldSaveMedication() {
        CreateMedicationRequest request = new CreateMedicationRequest(
                "Thor",
                "Amoxicilina 250mg",
                LocalDate.of(2026, 6, 30)
        );
        Medication entity = medication();
        Medication saved = medication();
        MedicationResponse response = medicationResponse();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        MedicationResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldGetAllMedications() {
        Medication entity = medication();
        MedicationResponse response = medicationResponse();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<MedicationResponse> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(response);
    }

    @Test
    void shouldGetMedicationById() {
        Medication entity = medication();
        MedicationResponse response = medicationResponse();

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        MedicationResponse result = service.getById(entity.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldDeleteMedication() {
        Medication entity = medication();

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        service.delete(entity.getId());

        verify(repository).delete(entity);
    }

    @Test
    void shouldThrowWhenMedicationNotFoundOnDelete() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(MedicationNotFoundException.class);
    }

    @Test
    void shouldThrowWhenMedicationNotFoundOnGetById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(MedicationNotFoundException.class);
    }

    private Medication medication() {
        Medication m = new Medication();
        m.setId(UUID.randomUUID());
        m.setDogsName("Thor");
        m.setPrescription("Amoxicilina 250mg");
        m.setLimitDate(LocalDate.of(2026, 6, 30));
        m.setCreatedAt(LocalDateTime.now());
        m.setUpdatedAt(LocalDateTime.now());
        return m;
    }

    private MedicationResponse medicationResponse() {
        return new MedicationResponse(
                UUID.randomUUID(),
                "Thor",
                "Amoxicilina 250mg",
                LocalDate.of(2026, 6, 30),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}

