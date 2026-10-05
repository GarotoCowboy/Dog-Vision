package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogReproductionRequest;
import br.com.dogvision.doghealth.dto.mapper.DogReproductionMapper;
import br.com.dogvision.doghealth.dto.response.DogReproductionResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogReproductionRequest;
import br.com.dogvision.doghealth.infra.exception.ReproductionNotFoundException;
import br.com.dogvision.doghealth.model.DogReproduction;
import br.com.dogvision.doghealth.repository.DogReproductionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogReproductionServiceImpTest {

    @Mock
    private DogReproductionRepository repository;

    @Mock
    private DogReproductionMapper mapper;

    private DogReproductionServiceImp service;

    @BeforeEach
    void setUp() {
        service = new DogReproductionServiceImp(repository, mapper);
    }

    @Test
    @DisplayName("Deve salvar registro de controle reprodutivo calculando automaticamente 6 meses para o próximo cio quando data não for informada")
    void shouldSaveDogReproductionWithDefault6MonthsIntervalWhenExpectedDateIsNull() {
        UUID veterinarianId = UUID.randomUUID();
        UUID dogId = UUID.randomUUID();
        LocalDate heatDate = LocalDate.of(2026, 4, 1);

        CreateDogReproductionRequest request = new CreateDogReproductionRequest(
                dogId,
                "Mel",
                "Golden Retriever",
                heatDate,
                null,
                null,
                "Primeiro cio registrado"
        );

        DogReproduction entity = new DogReproduction();
        DogReproduction saved = new DogReproduction();
        DogReproductionResponse response = mock(DogReproductionResponse.class);

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        DogReproductionResponse result = service.save(request, veterinarianId);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getVeterinarianId()).isEqualTo(veterinarianId);
        assertThat(entity.getCycleIntervalInMonths()).isEqualTo(6);
        assertThat(entity.getExpectedNextHeatDate()).isEqualTo(LocalDate.of(2026, 10, 1));
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve salvar registro com data de próximo cio informada explicitamente")
    void shouldSaveDogReproductionWithExplicitExpectedDate() {
        UUID veterinarianId = UUID.randomUUID();
        UUID dogId = UUID.randomUUID();
        LocalDate heatDate = LocalDate.of(2026, 4, 1);
        LocalDate explicitNextDate = LocalDate.of(2026, 9, 15);

        CreateDogReproductionRequest request = new CreateDogReproductionRequest(
                dogId,
                "Mel",
                "Golden Retriever",
                heatDate,
                explicitNextDate,
                5,
                "Ciclo irregular"
        );

        DogReproduction entity = new DogReproduction();
        DogReproduction saved = new DogReproduction();
        DogReproductionResponse response = mock(DogReproductionResponse.class);

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        DogReproductionResponse result = service.save(request, veterinarianId);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getExpectedNextHeatDate()).isEqualTo(explicitNextDate);
        assertThat(entity.getCycleIntervalInMonths()).isEqualTo(5);
    }

    @Test
    @DisplayName("Deve buscar registro por ID com sucesso")
    void shouldGetById() {
        UUID id = UUID.randomUUID();
        DogReproduction entity = new DogReproduction();
        DogReproductionResponse response = mock(DogReproductionResponse.class);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        DogReproductionResponse result = service.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("Deve lançar ReproductionNotFoundException quando ID não existir")
    void shouldThrowWhenNotFoundById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ReproductionNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("Deve listar todos os registros")
    void shouldListAll() {
        DogReproduction entity = new DogReproduction();
        DogReproductionResponse response = mock(DogReproductionResponse.class);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<DogReproductionResponse> result = service.getAll();

        assertThat(result).containsExactly(response);
    }

    @Test
    @DisplayName("Deve listar histórico de reprodução por dogId")
    void shouldListByDogId() {
        UUID dogId = UUID.randomUUID();
        DogReproduction entity = new DogReproduction();
        DogReproductionResponse response = mock(DogReproductionResponse.class);

        when(repository.findAllByDogIdOrderByDateDesc(dogId)).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<DogReproductionResponse> result = service.listByDogId(dogId);

        assertThat(result).containsExactly(response);
    }

    @Test
    @DisplayName("Deve buscar último cio da cadela por dogId")
    void shouldGetLastHeatByDogId() {
        UUID dogId = UUID.randomUUID();
        DogReproduction entity = new DogReproduction();
        DogReproductionResponse response = mock(DogReproductionResponse.class);

        when(repository.findTopByDogIdOrderByDateDesc(dogId)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        Optional<DogReproductionResponse> result = service.getLastHeatByDogId(dogId);

        assertThat(result).contains(response);
    }

    @Test
    @DisplayName("Deve atualizar registro e recalcular próximo cio caso a data mude sem nova previsão explícita")
    void shouldUpdateDogReproductionRecalculatingExpectedDate() {
        UUID id = UUID.randomUUID();
        LocalDate oldDate = LocalDate.of(2026, 1, 1);
        LocalDate newDate = LocalDate.of(2026, 2, 1);

        DogReproduction entity = new DogReproduction();
        entity.setId(id);
        entity.setDate(oldDate);
        entity.setCycleIntervalInMonths(6);
        entity.setExpectedNextHeatDate(LocalDate.of(2026, 7, 1));

        UpdateDogReproductionRequest updateRequest = new UpdateDogReproductionRequest(
                newDate,
                null,
                null,
                "Data ajustada"
        );

        DogReproductionResponse response = mock(DogReproductionResponse.class);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            entity.setDate(newDate);
            entity.setObservations("Data ajustada");
            return null;
        }).when(mapper).updateFromDto(updateRequest, entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        DogReproductionResponse result = service.update(id, updateRequest);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getExpectedNextHeatDate()).isEqualTo(LocalDate.of(2026, 8, 1));
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve deletar registro com sucesso")
    void shouldDeleteDogReproduction() {
        UUID id = UUID.randomUUID();
        DogReproduction entity = new DogReproduction();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.delete(id);

        verify(repository).delete(entity);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar registro inexistente")
    void shouldThrowWhenDeletingNonExistent() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ReproductionNotFoundException.class);
    }
}
