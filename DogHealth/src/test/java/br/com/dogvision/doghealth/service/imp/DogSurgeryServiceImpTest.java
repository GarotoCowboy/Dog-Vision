package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogSurgeryRequest;
import br.com.dogvision.doghealth.dto.mapper.DogSurgeryMapper;
import br.com.dogvision.doghealth.dto.response.DogSurgeryResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogSurgeryRequest;
import br.com.dogvision.doghealth.infra.exception.SurgeryNotFoundException;
import br.com.dogvision.doghealth.model.DogSurgery;
import br.com.dogvision.doghealth.model.EnumUrgency;
import br.com.dogvision.doghealth.repository.DogSurgeryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DogSurgeryServiceImpTest {

    @Mock
    private DogSurgeryRepository repository;

    @Mock
    private DogSurgeryMapper mapper;

    private DogSurgeryServiceImp service;

    @BeforeEach
    void setUp() {
        service = new DogSurgeryServiceImp(repository, mapper);
    }

    @Test
    void shouldReturnSurgeryById() {
        DogSurgery surgery = surgery();
        DogSurgeryResponse response = response();

        when(repository.findById(surgery.getId())).thenReturn(Optional.of(surgery));
        when(mapper.toResponse(surgery)).thenReturn(response);

        DogSurgeryResponse result = service.getById(surgery.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldSaveSurgeryWithVeterinarianId() {
        UUID veterinarianId = UUID.randomUUID();
        CreateDogSurgeryRequest request = new CreateDogSurgeryRequest(
                UUID.randomUUID(),
                "Thor",
                "Golden",
                "Tumor removal",
                LocalDateTime.now().plusDays(3),
                "2 hours",
                EnumUrgency.NORMAL,
                true,
                "Fasting required"
        );
        DogSurgery surgery = surgery();
        DogSurgery saved = surgery();
        DogSurgeryResponse response = response();

        when(mapper.toEntity(request)).thenReturn(surgery);
        when(repository.save(surgery)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        DogSurgeryResponse result = service.save(request, veterinarianId);

        assertThat(result).isEqualTo(response);
        assertThat(surgery.getVeterinarianId()).isEqualTo(veterinarianId);
    }

    @Test
    void shouldFilterSurgeryByDogAndPeriod() {
        UUID dogId = UUID.randomUUID();
        LocalDateTime startsAt = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime endsAt = LocalDateTime.of(2026, 5, 31, 23, 59, 59);
        DogSurgery surgery = surgery();
        DogSurgeryResponse response = response();
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("dateTimeOfSurgery").ascending());

        when(repository.findByDogIdAndDateTimeOfSurgeryBetween(dogId, startsAt, endsAt, pageable))
                .thenReturn(new PageImpl<>(List.of(surgery)));
        when(mapper.toResponse(surgery)).thenReturn(response);

        var result = service.findByDogIdAndDateTimeOfSurgeryBetween(dogId, startsAt, endsAt, 0, 10);

        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void shouldUpdateSurgery() {
        DogSurgery surgery = surgery();
        DogSurgeryResponse response = response();
        UpdateDogSurgeryRequest request = new UpdateDogSurgeryRequest(
                null,
                LocalDateTime.now().plusDays(4),
                "3 hours",
                EnumUrgency.HIGH,
                true,
                "Updated observation"
        );

        when(repository.findById(surgery.getId())).thenReturn(Optional.of(surgery));
        when(mapper.toResponse(surgery)).thenReturn(response);

        DogSurgeryResponse result = service.update(surgery.getId(), request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateFromDto(request, surgery);
        verify(repository).save(surgery);
    }

    @Test
    void shouldThrowWhenSurgeryDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(SurgeryNotFoundException.class);
    }

    private DogSurgery surgery() {
        DogSurgery surgery = new DogSurgery();
        surgery.setId(UUID.randomUUID());
        surgery.setVeterinarianId(UUID.randomUUID());
        surgery.setDogId(UUID.randomUUID());
        surgery.setDogsName("Thor");
        surgery.setDogsBreed("Golden");
        surgery.setTitle("Tumor removal");
        surgery.setDateTimeOfSurgery(LocalDateTime.now().plusDays(3));
        surgery.setDurationExpected("2 hours");
        surgery.setUrgency(EnumUrgency.NORMAL);
        surgery.setOnFasting(true);
        surgery.setObservation("Fasting required");
        surgery.setCreatedAt(LocalDateTime.now());
        surgery.setUpdatedAt(LocalDateTime.now());
        return surgery;
    }

    private DogSurgeryResponse response() {
        return new DogSurgeryResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Thor",
                "Golden",
                "Tumor removal",
                LocalDateTime.now().plusDays(3),
                "2 hours",
                EnumUrgency.NORMAL,
                true,
                "Fasting required",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
