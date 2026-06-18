package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.Feeding;
import br.com.dogvision.dogfeeding.model.FeedingItem;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
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
class FeedingItemRepositoryIntegrationTest {

    @Autowired
    private FeedingItemRepository feedingItemRepository;

    @Autowired
    private FeedingRepository feedingRepository;

    @Autowired
    private RationRepository rationRepository;

    @Test
    void shouldGenerateConsumptionReportAndCheckRationUsage() {
        UUID dogId = UUID.randomUUID();
        Ration ration = rationRepository.saveAndFlush(buildRation("Racao normal", RationType.NORMAL));

        Feeding feeding = new Feeding();
        feeding.setDogId(dogId);
        feeding.setDate(LocalDate.now());
        feeding.setFeedingTime(MealType.BREAKFAST.defaultTime());
        feeding.setMealType(MealType.BREAKFAST);
        feeding.setQuantity(0.8);

        FeedingItem item = new FeedingItem();
        item.setFeeding(feeding);
        item.setRation(ration);
        item.setQuantityUsed(0.8);
        item.setUnit(MeasurementUnit.KILOGRAM);
        feeding.setItems(List.of(item));
        feedingRepository.saveAndFlush(feeding);

        assertThat(feedingItemRepository.existsByRationId(ration.getId())).isTrue();
        assertThat(feedingItemRepository.findForConsumptionReport(
                dogId,
                RationType.NORMAL,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        )).singleElement()
                .extracting(FeedingItem::getQuantityUsed)
                .isEqualTo(0.8);
    }

    private static Ration buildRation(String name, RationType rationType) {
        Ration ration = new Ration();
        ration.setName(name);
        ration.setRationType(rationType);
        ration.setTotalRationQuantity(15);
        ration.setCurrentRationQuantity(10);
        ration.setRegistrationDate(LocalDate.now());
        return ration;
    }
}
