package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateEmployeeRequest;
import br.com.dogvision.user.dto.mapper.EmployeeMapper;
import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private EmployeeMapper mapper;

    private EmployeeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeServiceImpl(employeeRepository, userRepository, userService, mapper);
    }

    @Test
    void shouldReturnEmployeeById() {
        Employee employee = employee();
        EmployeeResponse response = employeeResponse();

        when(employeeRepository.findByIdWithUser(employee.getId())).thenReturn(Optional.of(employee));
        when(mapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = service.getById(employee.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldSaveEmployeeWithRoleBasedOnType() {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                "EMP001",
                ShiftEnum.NIGHT,
                EmployeeType.VETERINARIAN
        );
        User user = new User();
        user.setRegistration("EMP001");
        user.setRoles(Set.of(Role.ROLE_VETERINARIAN));

        Employee employee = employee();
        Employee saved = employee();
        EmployeeResponse response = employeeResponse();

        when(employeeRepository.existsByEmail("employee@dogvision.com")).thenReturn(false);
        when(userService.createAccount("EMP001", "employee@dogvision.com", "Maria Oliveira", Role.ROLE_VETERINARIAN))
                .thenReturn(user);
        when(mapper.toEntity(request)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        EmployeeResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
        assertThat(employee.getUser()).isEqualTo(user);
    }

    @Test
    void shouldRejectSaveWhenEmailAlreadyExists() {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                "EMP001",
                ShiftEnum.NIGHT,
                EmployeeType.VETERINARIAN
        );

        when(employeeRepository.existsByEmail("employee@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldUpdateEmployeeWhenEmailChanges() {
        Employee employee = employee();
        EmployeeResponse response = employeeResponse();
        UpdateEmployeeRequest request = new UpdateEmployeeRequest("new@dogvision.com", "New Name", "11888887777", ShiftEnum.MORNING);

        when(employeeRepository.findByIdWithUser(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("new@dogvision.com")).thenReturn(false);
        when(mapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = service.update(employee.getId(), request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateFromDto(request, employee);
    }

    @Test
    void shouldRejectUpdateWhenEmailAlreadyExists() {
        Employee employee = employee();
        UpdateEmployeeRequest request = new UpdateEmployeeRequest("new@dogvision.com", null, null, null);

        when(employeeRepository.findByIdWithUser(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("new@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.update(employee.getId(), request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldDeleteEmployeeUser() {
        Employee employee = employee();
        when(employeeRepository.findByIdWithUser(employee.getId())).thenReturn(Optional.of(employee));

        service.delete(employee.getId());

        verify(userRepository).delete(employee.getUser());
    }

    private Employee employee() {
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setEmail("employee@dogvision.com");
        employee.setName("Maria Oliveira");
        employee.setPhone("11987654321");
        employee.setShift(ShiftEnum.NIGHT);
        employee.setType(EmployeeType.VETERINARIAN);

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("EMP001");
        user.setPasswordHash("hashed-password");
        user.setActive(true);
        user.setRoles(Set.of(Role.ROLE_VETERINARIAN));
        employee.setUser(user);

        return employee;
    }

    private EmployeeResponse employeeResponse() {
        return new EmployeeResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "EMP001",
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                "NIGHT",
                EmployeeType.VETERINARIAN,
                true
        );
    }
}
