package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.Feeding;
import br.com.dogvision.dogfeeding.model.FeedingItem;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FeedingRepositoryIntegrationTest {

    @Autowired
    private FeedingRepository feedingRepository;

    @Autowired
    private RationRepository rationRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldLoadFeedingWithItemsAndFilterByDogAndPeriod() {
        UUID dogId = UUID.randomUUID();
        Ration ration = rationRepository.saveAndFlush(buildRation("Special", RationType.SPECIAL));

        Feeding feeding = new Feeding();
        feeding.setDogId(dogId);
        feeding.setDate(LocalDate.now());
        feeding.setFeedingTime(LocalTime.of(12, 0));
        feeding.setMealType(MealType.LUNCH);
        feeding.setQuantity(1.2);
        feeding.setNotes("Calm feeding");
        feeding.setDogResponse("Accepted well");

        FeedingItem item = new FeedingItem();
        item.setFeeding(feeding);
        item.setRation(ration);
        item.setQuantityUsed(1.2);
        item.setUnit(MeasurementUnit.KILOGRAM);
        feeding.setItems(List.of(item));

        Feeding savedFeeding = feedingRepository.saveAndFlush(feeding);
        entityManager.clear();

        assertThat(feedingRepository.findById(savedFeeding.getId()))
                .get()
                .satisfies(foundFeeding -> {
                    assertThat(foundFeeding.getItems()).hasSize(1);
                    assertThat(foundFeeding.getItems().getFirst().getRation().getName()).isEqualTo("Special");
                });
        assertThat(feedingRepository.findAllByDogIdAndDateBetween(dogId, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)))
                .hasSize(1);
        assertThat(feedingRepository.findAllByMealType(MealType.LUNCH)).hasSize(1);
    }

    private static Ration buildRation(String name, RationType rationType) {
        Ration ration = new Ration();
        ration.setName(name);
        ration.setRationType(rationType);
        ration.setTotalRationQuantity(20);
        ration.setCurrentRationQuantity(15);
        ration.setRegistrationDate(LocalDate.now());
        return ration;
    }
}
