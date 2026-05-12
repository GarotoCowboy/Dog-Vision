package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.mapper.DogBirthMapper;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogBirthRequest;
import br.com.dogvision.doghealth.infra.exception.BirthNotFoundException;
import br.com.dogvision.doghealth.model.DogBirth;
import br.com.dogvision.doghealth.repository.DogBirthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DogBirthServiceImpTest {

    @Mock
    private DogBirthRepository repository;

    @Mock
    private DogBirthMapper mapper;

    private DogBirthServiceImp service;

    @BeforeEach
    void setUp() {
        service = new DogBirthServiceImp(repository, mapper);
    }

    @Test
    void shouldSaveDogBirthWithVeterinarianId() {
        UUID veterinarianId = UUID.randomUUID();
        CreateDogBirthRequest request = new CreateDogBirthRequest(
                UUID.randomUUID(),
                "Luna",
                "Border Collie",
                LocalDateTime.now(),
                4,
                "Healthy puppies",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now()
        );
        DogBirth entity = dogBirth();
        DogBirth saved = dogBirth();
        DogBirthResponse response = dogBirthResponse();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        DogBirthResponse result = service.save(request, veterinarianId);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getVeterinarianId()).isEqualTo(veterinarianId);
    }

    @Test
    void shouldUpdateDogBirth() {
        DogBirth entity = dogBirth();
        DogBirthResponse response = dogBirthResponse();
        UpdateDogBirthRequest request = new UpdateDogBirthRequest(
                LocalDateTime.now(),
                5,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1),
                "Updated note"
        );

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        DogBirthResponse result = service.update(entity.getId(), request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateFromDto(request, entity);
        verify(repository).save(entity);
    }

    @Test
    void shouldDeleteDogBirth() {
        DogBirth entity = dogBirth();

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        service.delete(entity.getId());

        verify(repository).delete(entity);
    }

    @Test
    void shouldThrowWhenDogBirthDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(BirthNotFoundException.class);
    }

    private DogBirth dogBirth() {
        DogBirth birth = new DogBirth();
        birth.setId(UUID.randomUUID());
        birth.setDogId(UUID.randomUUID());
        birth.setVeterinarianId(UUID.randomUUID());
        birth.setDogsName("Luna");
        birth.setDogsBreed("Border Collie");
        birth.setDate(LocalDateTime.now());
        birth.setNumberOfPuppies(4);
        birth.setObservations("Healthy puppies");
        birth.setStartTime(LocalDateTime.now().minusHours(1));
        birth.setEndTime(LocalDateTime.now());
        return birth;
    }

    private DogBirthResponse dogBirthResponse() {
        return new DogBirthResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Luna",
                "Border Collie",
                LocalDateTime.now(),
                4,
                "Healthy puppies",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now()
        );
    }
}
