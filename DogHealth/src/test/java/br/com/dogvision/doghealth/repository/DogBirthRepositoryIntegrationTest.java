package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogBirth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DogBirthRepositoryIntegrationTest {

    @Autowired
    private DogBirthRepository dogBirthRepository;

    @Test
    void shouldPersistBirthRecordInH2() {
        DogBirth dogBirth = new DogBirth();
        dogBirth.setDogId(UUID.randomUUID());
        dogBirth.setDogsName("Mel");
        dogBirth.setDogsBreed("Golden");
        dogBirth.setVeterinarianId(UUID.randomUUID());
        dogBirth.setDate(LocalDateTime.now());
        dogBirth.setObservations("Everything went well");
        dogBirth.setNumberOfPuppies(5);
        dogBirth.setStartTime(LocalDateTime.now().minusHours(2));
        dogBirth.setEndTime(LocalDateTime.now().minusHours(1));

        DogBirth savedDogBirth = dogBirthRepository.saveAndFlush(dogBirth);

        assertThat(savedDogBirth.getId()).isNotNull();
        assertThat(dogBirthRepository.findById(savedDogBirth.getId())).isPresent();
    }
}
