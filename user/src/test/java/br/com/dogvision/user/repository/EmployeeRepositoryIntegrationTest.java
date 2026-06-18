package br.com.dogvision.user.repository;

import br.com.dogvision.user.model.Employee;
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
class EmployeeRepositoryIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldPersistEmployeeAndQueryWithUser() {
        Employee employee = new Employee();
        employee.setUser(buildUser("EMP-001", Role.ROLE_COLLABORATOR));
        employee.setName("Marina");
        employee.setShift(ShiftEnum.AFTERNOON);
        employee.setEmail("marina@dogvision.com");
        employee.setPhone("11999999999");
        employee.setType(EmployeeType.COLLABORATOR);

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);

        assertThat(employeeRepository.existsByEmail("marina@dogvision.com")).isTrue();
        assertThat(employeeRepository.findAllWithUser()).hasSize(1);
        assertThat(employeeRepository.findByIdWithUser(savedEmployee.getId()))
                .get()
                .extracting(Employee::getEmail)
                .isEqualTo("marina@dogvision.com");
    }

    private static User buildUser(String registration, Role role) {
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash("encoded-password");
        user.setRoles(Set.of(role));
        return user;
    }
}
