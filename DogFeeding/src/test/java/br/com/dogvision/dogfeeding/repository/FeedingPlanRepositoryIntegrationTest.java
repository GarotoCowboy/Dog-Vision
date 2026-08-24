package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.FeedingPlan;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FeedingPlanRepositoryIntegrationTest {

    @Autowired
    private FeedingPlanRepository feedingPlanRepository;

    @Test
    void shouldPersistMealTypesAndFilterByDogId() {
        UUID dogId = UUID.randomUUID();
        feedingPlanRepository.saveAndFlush(buildPlan(dogId, "Plano A"));
        feedingPlanRepository.saveAndFlush(buildPlan(UUID.randomUUID(), "Plano B"));

        assertThat(feedingPlanRepository.findAllByDogId(dogId))
                .singleElement()
                .satisfies(plan -> {
                    assertThat(plan.getName()).isEqualTo("Plano A");
                    assertThat(plan.getMealTypes()).containsExactly(MealType.BREAKFAST, MealType.DINNER);
                });
    }

    @Test
    void shouldFindActivePlansByRationIdFilteringInactiveOrExpired() {
        UUID rationId = UUID.randomUUID();
        LocalDate today = LocalDate.now();

        // 1. Active plan with no end date
        FeedingPlan activeNoEnd = buildPlan(UUID.randomUUID(), "Active No End");
        activeNoEnd.setRationId(rationId);
        activeNoEnd.setStartDate(today.minusDays(5));
        activeNoEnd.setEndDate(null);
        activeNoEnd.setActive(true);
        feedingPlanRepository.saveAndFlush(activeNoEnd);

        // 2. Active plan with future end date
        FeedingPlan activeFutureEnd = buildPlan(UUID.randomUUID(), "Active Future End");
        activeFutureEnd.setRationId(rationId);
        activeFutureEnd.setStartDate(today.minusDays(2));
        activeFutureEnd.setEndDate(today.plusDays(10));
        activeFutureEnd.setActive(true);
        feedingPlanRepository.saveAndFlush(activeFutureEnd);

        // 3. Expired plan
        FeedingPlan expired = buildPlan(UUID.randomUUID(), "Expired");
        expired.setRationId(rationId);
        expired.setStartDate(today.minusDays(20));
        expired.setEndDate(today.minusDays(1));
        expired.setActive(true);
        feedingPlanRepository.saveAndFlush(expired);

        // 4. Inactive flag plan
        FeedingPlan inactive = buildPlan(UUID.randomUUID(), "Inactive");
        inactive.setRationId(rationId);
        inactive.setStartDate(today.minusDays(5));
        inactive.setActive(false);
        feedingPlanRepository.saveAndFlush(inactive);

        // 5. Future start plan
        FeedingPlan futureStart = buildPlan(UUID.randomUUID(), "Future Start");
        futureStart.setRationId(rationId);
        futureStart.setStartDate(today.plusDays(5));
        futureStart.setActive(true);
        feedingPlanRepository.saveAndFlush(futureStart);

        List<FeedingPlan> result = feedingPlanRepository.findActivePlansByRationId(rationId, today);
        assertThat(result).hasSize(2);
        assertThat(result).extracting(FeedingPlan::getName).containsExactlyInAnyOrder("Active No End", "Active Future End");
    }

    private static FeedingPlan buildPlan(UUID dogId, String name) {
        FeedingPlan plan = new FeedingPlan();
        plan.setDogId(dogId);
        plan.setRationId(UUID.randomUUID());
        plan.setName(name);
        plan.setGoal("Maintenance");
        plan.setDailyQuantity(1.5);
        plan.setUnit(MeasurementUnit.KILOGRAM);
        plan.setMealTypes(List.of(MealType.BREAKFAST, MealType.DINNER));
        plan.setStartDate(LocalDate.now());
        plan.setNotes("Observacao");
        return plan;
    }
}
