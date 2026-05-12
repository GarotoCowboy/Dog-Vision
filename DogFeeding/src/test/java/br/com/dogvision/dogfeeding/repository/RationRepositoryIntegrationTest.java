package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RationRepositoryIntegrationTest {

    @Autowired
    private RationRepository rationRepository;

    @Test
    void shouldFilterRationByType() {
        rationRepository.saveAndFlush(buildRation("Adult", RationType.NORMAL));
        rationRepository.saveAndFlush(buildRation("Puppy", RationType.PUPPY));

        assertThat(rationRepository.findAllByRationType(RationType.PUPPY))
                .singleElement()
                .extracting(Ration::getName)
                .isEqualTo("Puppy");
    }

    private static Ration buildRation(String name, RationType rationType) {
        Ration ration = new Ration();
        ration.setName(name);
        ration.setRationType(rationType);
        ration.setTotalRationQuantity(10);
        ration.setCurrentRationQuantity(8);
        ration.setRegistrationDate(LocalDate.now());
        return ration;
    }
}
