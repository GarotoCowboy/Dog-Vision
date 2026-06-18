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
                0.45,
                MeasurementUnit.KILOGRAM,
                List.of(MealType.BREAKFAST, MealType.DINNER),
                "Dividir em duas porcoes",
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        ), UUID.randomUUID());

        assertThat(response.rationId()).isEqualTo(rationId);
        assertThat(response.rationName()).isEqualTo("Premium Senior");
        assertThat(response.rationType()).isEqualTo(RationType.SPECIAL);
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
                0.30,
                MeasurementUnit.KILOGRAM,
                List.of(MealType.LUNCH),
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
        entity.setDailyQuantity(0.4);
        entity.setUnit(MeasurementUnit.KILOGRAM);
        entity.setMealTypes(List.of(MealType.BREAKFAST));
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
                null
        ), UUID.randomUUID());

        assertThat(response.rationId()).isEqualTo(newRationId);
        assertThat(response.rationName()).isEqualTo("Premium Light");
    }

    private Ration ration(UUID id, String name) {
        Ration ration = new Ration();
        ration.setId(id);
        ration.setName(name);
        ration.setRationType(RationType.SPECIAL);
        ration.setTotalRationQuantity(10.0);
        ration.setCurrentRationQuantity(8.0);
        ration.setRegistrationDate(LocalDate.now().minusDays(5));
        return ration;
    }
}
