package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPersistAndFindUserByRegistration() {
        User user = buildUser("REG-001", Role.ROLE_COLLABORATOR);

        userRepository.saveAndFlush(user);

        assertThat(userRepository.existsByRegistration("REG-001")).isTrue();
        assertThat(userRepository.findByRegistration("REG-001"))
                .isInstanceOf(User.class)
                .extracting("username")
                .isEqualTo("REG-001");
    }

    private static User buildUser(String registration, Role role) {
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash("encoded-password");
        user.setRoles(Set.of(role));
        return user;
    }
}
