package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.Trainer;
import br.com.dogvision.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TrainerRepositoryIntegrationTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void shouldLoadTrainerWithUserRelationships() {
        Trainer trainer = new Trainer();
        trainer.setUser(buildUser("TRA-001", Role.ROLE_TRAINER));
        trainer.setName("Renato");
        trainer.setShift(ShiftEnum.NIGHT);
        trainer.setEmail("renato@dogvision.com");
        trainer.setPhone("11922222222");
        trainer.setType(EmployeeType.TRAINER);
        trainer.setAreaOfExpertise("Guide dogs");

        Trainer savedTrainer = trainerRepository.saveAndFlush(trainer);

        assertThat(trainerRepository.findAllWithUser()).hasSize(1);
        assertThat(trainerRepository.findByIdWithUser(savedTrainer.getId())).isPresent();
        assertThat(trainerRepository.findByRegistration("TRA-001"))
                .get()
                .extracting(Trainer::getAreaOfExpertise)
                .isEqualTo("Guide dogs");
    }

    private static User buildUser(String registration, Role role) {
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash("encoded-password");
        user.setRoles(Set.of(role));
        return user;
    }
}
