package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.model.Veterinarian;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class VeterinarianRepositoryIntegrationTest {

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Test
    void shouldQueryVeterinarianByCrmvAndRegistration() {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setUser(buildUser("VET-001", Role.ROLE_VETERINARIAN));
        veterinarian.setName("Helena");
        veterinarian.setShift(ShiftEnum.AFTERNOON);
        veterinarian.setEmail("helena@dogvision.com");
        veterinarian.setPhone("11933333333");
        veterinarian.setType(EmployeeType.VETERINARIAN);
        veterinarian.setCrmv("CRMV-123");
        veterinarian.setAreaOfExpertise("Neurology");

        Veterinarian savedVeterinarian = veterinarianRepository.saveAndFlush(veterinarian);

        assertThat(veterinarianRepository.existsByCrmv("CRMV-123")).isTrue();
        assertThat(veterinarianRepository.findAllWithUser()).hasSize(1);
        assertThat(veterinarianRepository.findByIdWithUser(savedVeterinarian.getId())).isPresent();
        assertThat(veterinarianRepository.findByRegistration("VET-001"))
                .get()
                .extracting(Veterinarian::getCrmv)
                .isEqualTo("CRMV-123");
    }

    private static User buildUser(String registration, Role role) {
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash("encoded-password");
        user.setRoles(Set.of(role));
        return user;
    }
}
