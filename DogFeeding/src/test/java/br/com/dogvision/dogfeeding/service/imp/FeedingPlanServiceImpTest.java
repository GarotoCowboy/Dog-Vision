package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.infra.exception.InvalidRationStateException;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingPlanRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedingPlanServiceImpTest {

    @Mock
    private FeedingPlanRepository repository;

    @Mock
    private RationRepository rationRepository;

    private FeedingPlanServiceImp service;

    @BeforeEach
    void setUp() {
        service = new FeedingPlanServiceImp(repository, rationRepository);
    }

    @Test
    void shouldCreatePlanUsingRegisteredRation() {
        UUID rationId = UUID.randomUUID();
        Ration ration = ration(rationId, "Premium Senior");

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(ration));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.save(new CreateFeedingPlanRequest(
                UUID.randomUUID(),
                rationId,
                "Plano Senior",
                "Controle de peso",
                250.0,
                MeasurementUnit.GRAM,
                List.of(MealType.BREAKFAST, MealType.DINNER),
                "Sem frango",
                "Dividir em duas porcoes",
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        ), UUID.randomUUID());

        assertThat(response.rationId()).isEqualTo(rationId);
        assertThat(response.rationName()).isEqualTo("Premium Senior");
        assertThat(response.rationType()).isEqualTo(RationType.SPECIAL);
        assertThat(response.mealQuantityInGrams()).isEqualTo(250.0);
        assertThat(response.dietaryRestriction()).isEqualTo("Sem frango");
    }

    @Test
    void shouldRejectPlanWhenRationIsNotRegistered() {
        UUID rationId = UUID.randomUUID();
        when(rationRepository.findById(rationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.save(new CreateFeedingPlanRequest(
                UUID.randomUUID(),
                rationId,
                "Plano",
                "Meta",
                150.0,
                MeasurementUnit.GRAM,
                List.of(MealType.LUNCH),
                null,
                null,
                LocalDate.now(),
                null
        ), UUID.randomUUID())).isInstanceOf(InvalidRationStateException.class);
    }

    @Test
    void shouldUpdatePlanRationWhenNewRegisteredRationIsProvided() {
        UUID currentRationId = UUID.randomUUID();
        UUID newRationId = UUID.randomUUID();
        Ration newRation = ration(newRationId, "Premium Light");

        var entity = new br.com.dogvision.dogfeeding.model.FeedingPlan();
        entity.setId(UUID.randomUUID());
        entity.setDogId(UUID.randomUUID());
        entity.setRationId(currentRationId);
        entity.setName("Plano");
        entity.setGoal("Meta");
        entity.setMealQuantityInGrams(200.0);
        entity.setUnit(MeasurementUnit.GRAM);
        entity.setMealTypes(List.of(MealType.BREAKFAST));
        entity.setDietaryRestriction("Sem lactose");
        entity.setStartDate(LocalDate.now());
        entity.setActive(true);

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(rationRepository.findById(newRationId)).thenReturn(Optional.of(newRation));

        var response = service.update(entity.getId(), new UpdateFeedingPlanRequest(
                newRationId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        ), UUID.randomUUID());

        assertThat(response.rationId()).isEqualTo(newRationId);
        assertThat(response.rationName()).isEqualTo("Premium Light");
    }

    @Test
    void shouldCreatePlanWithOptionalDietaryRestrictionAndNotesNull() {
        UUID rationId = UUID.randomUUID();
        Ration ration = ration(rationId, "Premium Senior");

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(ration));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.save(new CreateFeedingPlanRequest(
                UUID.randomUUID(),
                rationId,
                "Plano Basico",
                "Manutencao",
                300.0,
                null,
                List.of(MealType.BREAKFAST),
                null,
                null,
                LocalDate.now(),
                null
        ), UUID.randomUUID());

        assertThat(response.mealQuantityInGrams()).isEqualTo(300.0);
        assertThat(response.unit()).isEqualTo(MeasurementUnit.GRAM);
        assertThat(response.dietaryRestriction()).isNull();
        assertThat(response.notes()).isNull();
    }

    private Ration ration(UUID id, String name) {
        Ration ration = new Ration();
        ration.setId(id);
        ration.setName(name);
        ration.setRationType(RationType.SPECIAL);
        ration.setCurrentRationQuantity(8.0);
        ration.setRegistrationDate(LocalDate.now().minusDays(5));
        return ration;
    }
}
