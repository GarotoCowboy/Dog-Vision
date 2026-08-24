package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.mapper.RationMapper;
import br.com.dogvision.dogfeeding.dto.response.RationConsumptionEstimateResponse;
import br.com.dogvision.dogfeeding.infra.exception.InvalidRationStateException;
import br.com.dogvision.dogfeeding.infra.rabbit.ration.RationEventPublisher;
import br.com.dogvision.dogfeeding.model.FeedingPlan;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingItemRepository;
import br.com.dogvision.dogfeeding.repository.FeedingPlanRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RationServiceImpTest {

    @Mock
    private RationRepository rationRepository;

    @Mock
    private FeedingItemRepository feedingItemRepository;

    @Mock
    private FeedingPlanRepository feedingPlanRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RationEventPublisher rationEventPublisher;

    private RationServiceImp service;

    private final RationMapper rationMapper = Mappers.getMapper(RationMapper.class);

    @BeforeEach
    void setUp() {
        service = new RationServiceImp(rationRepository, feedingItemRepository, feedingPlanRepository, rationMapper, rabbitTemplate, rationEventPublisher);
    }

    @Test
    void shouldRejectNegativeCurrentStock() {
        CreateRationRequest request = new CreateRationRequest(
                "Premium",
                RationType.NORMAL,
                -5.0,
                LocalDate.now()
        );

        assertThatThrownBy(() -> service.save(request, null))
                .isInstanceOf(InvalidRationStateException.class);
    }

    @Test
    void shouldReturnOnlyMatchingStockStatus() {
        Ration healthy = ration("Healthy", 8.0);
        Ration low = ration("Low", 1.0);

        when(rationRepository.findAll()).thenReturn(List.of(healthy, low));

        List<?> result = service.search(null, RationStockStatus.LOW);

        assertThat(result).hasSize(1);
        assertThat(service.search(null, RationStockStatus.LOW).getFirst().name()).isEqualTo("Low");
    }

    @Test
    void shouldPersistValidRation() {
        when(rationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.save(new CreateRationRequest(
                "Premium",
                RationType.NORMAL,
                8.0,
                LocalDate.now()
        ), null);

        assertThat(response.stockStatus()).isEqualTo(RationStockStatus.HEALTHY);
    }

    @Test
    void shouldReturnOnlyStockAlerts() {
        Ration healthy = ration("Healthy", 8.0);
        Ration low = ration("Low", 1.0);
        Ration empty = ration("Empty", 0.0);

        when(rationRepository.findAll()).thenReturn(List.of(healthy, low, empty));

        var alerts = service.alerts();

        assertThat(alerts).hasSize(2);
        assertThat(alerts).extracting("rationName").containsExactly("Empty", "Low");
    }

    @Test
    void shouldCalculateEstimateWithActivePlans() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Adult Dog", 15.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));

        UUID dog1 = UUID.randomUUID();
        UUID dog2 = UUID.randomUUID();

        FeedingPlan plan1 = new FeedingPlan();
        plan1.setId(UUID.randomUUID());
        plan1.setDogId(dog1);
        plan1.setRationId(rationId);
        plan1.setName("Plan Dog 1");
        plan1.setDailyQuantity(1.0);
        plan1.setUnit(MeasurementUnit.KILOGRAM);
        plan1.setActive(true);
        plan1.setStartDate(LocalDate.now().minusDays(10));

        FeedingPlan plan2 = new FeedingPlan();
        plan2.setId(UUID.randomUUID());
        plan2.setDogId(dog2);
        plan2.setRationId(rationId);
        plan2.setName("Plan Dog 2");
        plan2.setDailyQuantity(0.5);
        plan2.setUnit(MeasurementUnit.KILOGRAM);
        plan2.setActive(true);
        plan2.setStartDate(LocalDate.now().minusDays(5));

        when(feedingPlanRepository.findActivePlansByRationId(eq(rationId), any(LocalDate.class)))
                .thenReturn(List.of(plan1, plan2));

        RationConsumptionEstimateResponse estimate = service.getEstimate(rationId);

        assertThat(estimate.rationId()).isEqualTo(rationId);
        assertThat(estimate.currentRationQuantityKg()).isEqualTo(15.0);
        assertThat(estimate.totalDailyConsumptionKg()).isEqualTo(1.5);
        assertThat(estimate.estimatedDaysRemaining()).isEqualTo(10.0);
        assertThat(estimate.estimatedDepletionDate()).isEqualTo(LocalDate.now().plusDays(10));
        assertThat(estimate.dogConsumptions()).hasSize(2);
        assertThat(estimate.dogConsumptions().get(0).dogId()).isEqualTo(dog1);
        assertThat(estimate.dogConsumptions().get(0).dailyQuantityKg()).isEqualTo(1.0);
        assertThat(estimate.dogConsumptions().get(1).dogId()).isEqualTo(dog2);
        assertThat(estimate.dogConsumptions().get(1).dailyQuantityKg()).isEqualTo(0.5);
    }

    @Test
    void shouldReturnNullDurationWhenNoActivePlans() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Puppy", 20.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));
        when(feedingPlanRepository.findActivePlansByRationId(eq(rationId), any(LocalDate.class)))
                .thenReturn(List.of());

        RationConsumptionEstimateResponse estimate = service.getEstimate(rationId);

        assertThat(estimate.totalDailyConsumptionKg()).isEqualTo(0.0);
        assertThat(estimate.estimatedDaysRemaining()).isNull();
        assertThat(estimate.estimatedDepletionDate()).isNull();
        assertThat(estimate.dogConsumptions()).isEmpty();
    }

    @Test
    void shouldReturnZeroDaysRemainingWhenOutOfStock() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Empty", 0.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));
        when(feedingPlanRepository.findActivePlansByRationId(eq(rationId), any(LocalDate.class)))
                .thenReturn(List.of());

        RationConsumptionEstimateResponse estimate = service.getEstimate(rationId);

        assertThat(estimate.estimatedDaysRemaining()).isEqualTo(0.0);
        assertThat(estimate.estimatedDepletionDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldIncreaseRationStockByBagsAndWeight() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Adult Dog", 10.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));
        when(rationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.increaseRation(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.IncreaseRationStockRequest(3, 15.0),
                UUID.randomUUID()
        );

        assertThat(response.currentRationQuantity()).isEqualTo(55.0);
    }

    @Test
    void shouldRejectIncreaseWithInvalidBagCountOrWeight() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Adult Dog", 10.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> service.increaseRation(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.IncreaseRationStockRequest(0, 15.0),
                UUID.randomUUID()
        )).isInstanceOf(InvalidRationStateException.class);

        assertThatThrownBy(() -> service.increaseRation(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.IncreaseRationStockRequest(2, -5.0),
                UUID.randomUUID()
        )).isInstanceOf(InvalidRationStateException.class);
    }

    @Test
    void shouldDecreaseRationStock() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Adult Dog", 10.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));
        when(rationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.decreaseRation(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest(3.0),
                UUID.randomUUID()
        );

        assertThat(response.currentRationQuantity()).isEqualTo(7.0);
    }

    @Test
    void shouldClampStockToZeroWhenDecreaseExceedsCurrentQuantity() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Adult Dog", 5.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));
        when(rationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.decreaseRation(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest(10.0),
                UUID.randomUUID()
        );

        assertThat(response.currentRationQuantity()).isEqualTo(0.0);
    }

    @Test
    void shouldRejectDecreaseWithNegativeOrZeroAmount() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Adult Dog", 5.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> service.decreaseRation(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest(0.0),
                UUID.randomUUID()
        )).isInstanceOf(InvalidRationStateException.class);

        assertThatThrownBy(() -> service.decreaseRation(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest(-2.0),
                UUID.randomUUID()
        )).isInstanceOf(InvalidRationStateException.class);
    }

    @Test
    void shouldUpdateRationWithoutChangingStock() {
        UUID rationId = UUID.randomUUID();
        Ration r = ration("Original Name", 15.0);
        r.setId(rationId);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(r));
        when(rationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.update(
                rationId,
                new br.com.dogvision.dogfeeding.dto.update.UpdateRationRequest("Updated Name", RationType.SPECIAL, LocalDate.now()),
                UUID.randomUUID()
        );

        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.rationType()).isEqualTo(RationType.SPECIAL);
        assertThat(response.currentRationQuantity()).isEqualTo(15.0);
    }

    private Ration ration(String name, double current) {
        Ration ration = new Ration();
        ration.setName(name);
        ration.setRationType(RationType.NORMAL);
        ration.setCurrentRationQuantity(current);
        ration.setRegistrationDate(LocalDate.now().minusDays(1));
        return ration;
    }
}
