package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogMassageRequest;
import br.com.dogvision.doghealth.dto.mapper.DogMassageMapper;
import br.com.dogvision.doghealth.dto.response.DogMassageResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogMassageRequest;
import br.com.dogvision.doghealth.infra.exception.MassageNotFoundException;
import br.com.dogvision.doghealth.model.DogMassage;
import br.com.dogvision.doghealth.repository.DogMassageRepository;
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
class DogMassageServiceImpTest {

    @Mock
    private DogMassageRepository repository;

    @Mock
    private DogMassageMapper mapper;

    private DogMassageServiceImp service;

    @BeforeEach
    void setUp() {
        service = new DogMassageServiceImp(repository, mapper);
    }

    @Test
    void shouldFilterMassageRecordsByWeek() {
        UUID dogId = UUID.randomUUID();
        LocalDate referenceDate = LocalDate.of(2026, 5, 13);
        LocalDateTime expectedStart = LocalDateTime.of(2026, 5, 11, 0, 0);
        LocalDateTime expectedEnd = LocalDateTime.of(2026, 5, 17, 0, 0);
        DogMassage entity = dogMassage();
        DogMassageResponse response = dogMassageResponse();

        when(repository.findAllByDogIdAndCreatedAtBetween(dogId, expectedStart, expectedEnd))
                .thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<DogMassageResponse> result = service.getByWeek(dogId, referenceDate);

        assertThat(result).containsExactly(response);
    }

    @Test
    void shouldReturnLastMassageWhenPresent() {
        UUID dogId = UUID.randomUUID();
        DogMassage entity = dogMassage();
        DogMassageResponse response = dogMassageResponse();

        when(repository.findTopByDogIdOrderByCreatedAtDesc(dogId)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        Optional<DogMassageResponse> result = service.getLastMassage(dogId);

        assertThat(result).contains(response);
    }

    @Test
    void shouldSaveMassageWithCollaboratorId() {
        UUID collaboratorId = UUID.randomUUID();
        CreateDogMassageRequest request = new CreateDogMassageRequest(
                UUID.randomUUID(),
                LocalDateTime.now(),
                "Relaxed after massage"
        );
        DogMassage entity = dogMassage();
        DogMassage saved = dogMassage();
        DogMassageResponse response = dogMassageResponse();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        DogMassageResponse result = service.save(request, collaboratorId);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getCollaboratorId()).isEqualTo(collaboratorId);
    }

    @Test
    void shouldUpdateMassage() {
        DogMassage entity = dogMassage();
        DogMassageResponse response = dogMassageResponse();
        UpdateDogMassageRequest request = new UpdateDogMassageRequest(LocalDateTime.now(), "Updated note");

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        DogMassageResponse result = service.update(entity.getId(), request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateFromDto(request, entity);
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowWhenMassageDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, new UpdateDogMassageRequest(null, null)))
                .isInstanceOf(MassageNotFoundException.class);
    }

    private DogMassage dogMassage() {
        DogMassage massage = new DogMassage();
        massage.setId(UUID.randomUUID());
        massage.setDogId(UUID.randomUUID());
        massage.setCollaboratorId(UUID.randomUUID());
        massage.setObservations("Relaxed after massage");
        massage.setCreatedAt(LocalDateTime.now());
        massage.setUpdatedAt(LocalDateTime.now());
        return massage;
    }

    private DogMassageResponse dogMassageResponse() {
        return new DogMassageResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Relaxed after massage",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
