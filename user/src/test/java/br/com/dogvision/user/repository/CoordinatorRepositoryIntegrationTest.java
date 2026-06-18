package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.Coordinator;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CoordinatorRepositoryIntegrationTest {

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @Test
    void shouldFindCoordinatorByRegistration() {
        Coordinator coordinator = new Coordinator();
        coordinator.setUser(buildUser("COO-001", Role.ROLE_COORDINATOR));
        coordinator.setName("Bianca");
        coordinator.setShift(ShiftEnum.MORNING);
        coordinator.setEmail("bianca@dogvision.com");
        coordinator.setPhone("11911111111");
        coordinator.setType(EmployeeType.COORDINATOR);

        coordinatorRepository.saveAndFlush(coordinator);

        assertThat(coordinatorRepository.findByRegistration("COO-001"))
                .get()
                .extracting(Coordinator::getEmail)
                .isEqualTo("bianca@dogvision.com");
    }

    private static User buildUser(String registration, Role role) {
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash("encoded-password");
        user.setRoles(Set.of(role));
        return user;
    }
}
