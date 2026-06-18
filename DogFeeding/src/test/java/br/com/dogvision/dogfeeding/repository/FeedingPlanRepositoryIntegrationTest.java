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
