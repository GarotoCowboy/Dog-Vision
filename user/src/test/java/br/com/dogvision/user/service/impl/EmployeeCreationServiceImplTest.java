package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeCreationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeCreationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeCreationServiceImpl(userService, employeeRepository);
    }

    @Test
    void shouldCreateEmployeeDelegatingToUserService() {
        User user = new User();
        user.setRegistration("EMP001");

        when(userService.createAccount("EMP001", "employee@dogvision.com", "Maria Oliveira", Role.ROLE_COLLABORATOR))
                .thenReturn(user);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = service.createEmployee(
                "EMP001",
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                ShiftEnum.AFTERNOON,
                EmployeeType.COLLABORATOR,
                Role.ROLE_COLLABORATOR
        );

        assertThat(result.getEmail()).isEqualTo("employee@dogvision.com");
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getType()).isEqualTo(EmployeeType.COLLABORATOR);
    }
}
