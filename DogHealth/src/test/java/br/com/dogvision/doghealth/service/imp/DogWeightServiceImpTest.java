package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.mapper.DogWeightMapper;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogWeightRequest;
import br.com.dogvision.doghealth.infra.exception.WeightNotFoundException;
import br.com.dogvision.doghealth.model.DogWeight;
import br.com.dogvision.doghealth.repository.DogWeightRepository;
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
class DogWeightServiceImpTest {

    @Mock
    private DogWeightRepository repository;

    @Mock
    private DogWeightMapper mapper;

    private DogWeightServiceImp service;

    @BeforeEach
    void setUp() {
        service = new DogWeightServiceImp(repository, mapper);
    }

    @Test
    void shouldFilterWeightRecordsByWeek() {
        UUID dogId = UUID.randomUUID();
        LocalDate referenceDate = LocalDate.of(2026, 5, 13);
        LocalDateTime expectedStart = LocalDateTime.of(2026, 5, 11, 0, 0);
        LocalDateTime expectedEnd = LocalDateTime.of(2026, 5, 17, 0, 0);
        DogWeight entity = dogWeight();
        DogWeightResponse response = dogWeightResponse();

        when(repository.findAllByDogIdAndCreatedAtBetween(dogId, expectedStart, expectedEnd))
                .thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<DogWeightResponse> result = service.getByWeek(dogId, referenceDate);

        assertThat(result).containsExactly(response);
    }

    @Test
    void shouldReturnLastWeightWhenPresent() {
        UUID dogId = UUID.randomUUID();
        DogWeight entity = dogWeight();
        DogWeightResponse response = dogWeightResponse();

        when(repository.findTopByDogIdOrderByCreatedAtDesc(dogId)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        Optional<DogWeightResponse> result = service.getLastWeight(dogId);

        assertThat(result).contains(response);
    }

    @Test
    void shouldSaveWeightWithCollaboratorId() {
        UUID collaboratorId = UUID.randomUUID();
        CreateDogWeightRequest request = new CreateDogWeightRequest(UUID.randomUUID(), 18.7);
        DogWeight entity = dogWeight();
        DogWeight saved = dogWeight();
        DogWeightResponse response = dogWeightResponse();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        DogWeightResponse result = service.save(request, collaboratorId);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getCollaboratorId()).isEqualTo(collaboratorId);
    }

    @Test
    void shouldUpdateWeight() {
        DogWeight entity = dogWeight();
        DogWeightResponse response = dogWeightResponse();
        UpdateDogWeightRequest request = new UpdateDogWeightRequest(19.2);

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        DogWeightResponse result = service.update(entity.getId(), request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateFromDto(request, entity);
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowWhenWeightDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(WeightNotFoundException.class);
    }

    private DogWeight dogWeight() {
        DogWeight dogWeight = new DogWeight();
        dogWeight.setId(UUID.randomUUID());
        dogWeight.setDogId(UUID.randomUUID());
        dogWeight.setCollaboratorId(UUID.randomUUID());
        dogWeight.setWeight(18.7);
        dogWeight.setCreatedAt(LocalDateTime.now());
        dogWeight.setUpdatedAt(LocalDateTime.now());
        return dogWeight;
    }

    private DogWeightResponse dogWeightResponse() {
        return new DogWeightResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                18.7,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
