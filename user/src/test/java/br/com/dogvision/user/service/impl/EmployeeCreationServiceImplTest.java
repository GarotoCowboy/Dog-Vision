package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeCreationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private EmployeeCreationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeCreationServiceImpl(userRepository, employeeRepository, passwordEncoder);
    }

    @Test
    void shouldCreateEmployeeWithEncodedPassword() {
        when(userRepository.existsByRegistration("EMP001")).thenReturn(false);
        when(employeeRepository.existsByEmail("employee@dogvision.com")).thenReturn(false);
        when(passwordEncoder.encode("password@123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = service.createEmployee(
                "EMP001",
                "password@123",
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                ShiftEnum.AFTERNOON,
                EmployeeType.COLLABORATOR,
                Role.ROLE_COLLABORATOR
        );

        assertThat(result.getEmail()).isEqualTo("employee@dogvision.com");
        assertThat(result.getUser().getPasswordHash()).isEqualTo("encoded-password");
        assertThat(result.getUser().getRoles()).containsExactly(Role.ROLE_COLLABORATOR);
        assertThat(result.getType()).isEqualTo(EmployeeType.COLLABORATOR);
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        when(userRepository.existsByRegistration("EMP001")).thenReturn(true);

        assertThatThrownBy(() -> service.createEmployee(
                "EMP001",
                "password@123",
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                ShiftEnum.AFTERNOON,
                EmployeeType.COLLABORATOR,
                Role.ROLE_COLLABORATOR
        )).isInstanceOfSatisfying(ResponseStatusException.class, ex ->
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(userRepository.existsByRegistration("EMP001")).thenReturn(false);
        when(employeeRepository.existsByEmail("employee@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.createEmployee(
                "EMP001",
                "password@123",
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                ShiftEnum.AFTERNOON,
                EmployeeType.COLLABORATOR,
                Role.ROLE_COLLABORATOR
        )).isInstanceOfSatisfying(ResponseStatusException.class, ex ->
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
    }
}
