package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.Consultation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ConsultationRepositoryIntegrationTest {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Test
    void shouldFilterConsultationByDogIdAndDateRange() {
        UUID dogId = UUID.randomUUID();
        consultationRepository.saveAndFlush(buildConsultation(dogId, "Otitis"));
        consultationRepository.saveAndFlush(buildConsultation(UUID.randomUUID(), "Checkup"));

        assertThat(consultationRepository.findAllByDogIdAndCreatedAtBetween(
                dogId,
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5)
        )).singleElement()
                .extracting(Consultation::getDiagnosis)
                .isEqualTo("Otitis");
    }

    private static Consultation buildConsultation(UUID dogId, String diagnosis) {
        Consultation consultation = new Consultation();
        consultation.setVeterinarianId(UUID.randomUUID());
        consultation.setDogId(dogId);
        consultation.setDogsName("Thor");
        consultation.setDogsBreed("Labrador");
        consultation.setTreatment("Medication");
        consultation.setDiagnosis(diagnosis);
        return consultation;
    }
}
