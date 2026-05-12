package br.com.dogvision.dogmanagement.repository;

import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DogRepositoryIntegrationTest {

    @Autowired
    private DogRepository dogRepository;

    @Test
    void shouldPersistAndLoadDogUsingH2() {
        Dog dog = new Dog();
        dog.setName("Luna");
        dog.setRace(DogRace.LABRADOR);
        dog.setStatus(DogStatus.SOCIALIZACAO);
        dog.setSex('F');
        dog.setDateOfBirth(Timestamp.valueOf("2024-01-10 08:30:00"));

        Dog savedDog = dogRepository.saveAndFlush(dog);

        assertThat(savedDog.getID()).isNotNull();
        assertThat(dogRepository.findById(savedDog.getID()))
                .get()
                .extracting(Dog::getName, Dog::getRace, Dog::getStatus)
                .containsExactly("Luna", DogRace.LABRADOR, DogStatus.SOCIALIZACAO);
    }
}
