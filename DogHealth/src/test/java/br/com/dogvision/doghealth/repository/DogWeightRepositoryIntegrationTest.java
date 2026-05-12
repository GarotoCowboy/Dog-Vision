package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogWeight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DogWeightRepositoryIntegrationTest {

    @Autowired
    private DogWeightRepository dogWeightRepository;

    @Test
    void shouldReturnLatestWeightAndFilterByDateRange() throws InterruptedException {
        UUID dogId = UUID.randomUUID();
        dogWeightRepository.saveAndFlush(buildWeight(dogId, 22.3));
        Thread.sleep(20L);
        dogWeightRepository.saveAndFlush(buildWeight(dogId, 23.1));

        assertThat(dogWeightRepository.findAllByDogId(dogId)).hasSize(2);
        assertThat(dogWeightRepository.findAllByDogIdAndCreatedAtBetween(
                dogId,
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5)
        )).hasSize(2);
        assertThat(dogWeightRepository.findTopByDogIdOrderByCreatedAtDesc(dogId))
                .get()
                .extracting(DogWeight::getWeight)
                .isEqualTo(23.1);
    }

    private static DogWeight buildWeight(UUID dogId, double weight) {
        DogWeight dogWeight = new DogWeight();
        dogWeight.setDogId(dogId);
        dogWeight.setCollaboratorId(UUID.randomUUID());
        dogWeight.setWeight(weight);
        return dogWeight;
    }
}
