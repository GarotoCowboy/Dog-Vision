package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.Collaborator;
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
class CollaboratorRepositoryIntegrationTest {

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Test
    void shouldFetchCollaboratorWithUserByRegistration() {
        Collaborator collaborator = new Collaborator();
        collaborator.setUser(buildUser("COL-001", Role.ROLE_COLLABORATOR));
        collaborator.setName("Carlos");
        collaborator.setShift(ShiftEnum.MORNING);

        Collaborator savedCollaborator = collaboratorRepository.saveAndFlush(collaborator);

        assertThat(collaboratorRepository.findAllWithUser())
                .hasSize(1)
                .first()
                .extracting(Collaborator::getName)
                .isEqualTo("Carlos");
        assertThat(collaboratorRepository.findByIdWithUser(savedCollaborator.getId())).isPresent();
        assertThat(collaboratorRepository.findByRegistration("COL-001"))
                .get()
                .extracting(Collaborator::getName)
                .isEqualTo("Carlos");
    }

    private static User buildUser(String registration, Role role) {
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash("encoded-password");
        user.setRoles(Set.of(role));
        return user;
    }
}
