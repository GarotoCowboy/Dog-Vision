package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogMassage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DogMassageRepositoryIntegrationTest {

    @Autowired
    private DogMassageRepository dogMassageRepository;

    @Test
    void shouldReturnLatestMassageAndFilterByRange() throws InterruptedException {
        UUID dogId = UUID.randomUUID();
        dogMassageRepository.saveAndFlush(buildMassage(dogId, "First"));
        Thread.sleep(20L);
        dogMassageRepository.saveAndFlush(buildMassage(dogId, "Latest"));

        assertThat(dogMassageRepository.findAllByDogId(dogId)).hasSize(2);
        assertThat(dogMassageRepository.findAllByDogIdAndCreatedAtBetween(
                dogId,
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5)
        )).hasSize(2);
        assertThat(dogMassageRepository.findTopByDogIdOrderByCreatedAtDesc(dogId))
                .get()
                .extracting(DogMassage::getObservations)
                .isEqualTo("Latest");
    }

    private static DogMassage buildMassage(UUID dogId, String observations) {
        DogMassage massage = new DogMassage();
        massage.setDogId(dogId);
        massage.setCollaboratorId(UUID.randomUUID());
        massage.setObservations(observations);
        return massage;
    }
}
