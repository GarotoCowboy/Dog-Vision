package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.mapper.CoordinatorMapper;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.model.Coordinator;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.CoordinatorRepository;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoordinatorServiceImplTest {

    @Mock
    private CoordinatorRepository coordinatorRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CoordinatorMapper coordinatorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CoordinatorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CoordinatorServiceImpl(
                coordinatorRepository,
                employeeRepository,
                userRepository,
                coordinatorMapper,
                passwordEncoder
        );
    }

    @Test
    void shouldReturnCoordinatorById() {
        Coordinator coordinator = coordinator();
        CoordinatorResponse response = coordinatorResponse();

        when(coordinatorRepository.findById(coordinator.getId())).thenReturn(Optional.of(coordinator));
        when(coordinatorMapper.toResponse(coordinator)).thenReturn(response);

        CoordinatorResponse result = service.getById(coordinator.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldSaveCoordinatorEncodingPasswordAndAssigningRole() {
        CreateCoordinatorRequest request = new CreateCoordinatorRequest(
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "COORD001",
                "password@123",
                ShiftEnum.MORNING
        );
        Coordinator coordinator = coordinator();
        Coordinator saved = coordinator();
        CoordinatorResponse response = coordinatorResponse();

        coordinator.getUser().setPasswordHash("password@123");

        when(userRepository.existsByRegistration("COORD001")).thenReturn(false);
        when(employeeRepository.existsByEmail("coordinator@dogvision.com")).thenReturn(false);
        when(coordinatorMapper.toEntity(request)).thenReturn(coordinator);
        when(passwordEncoder.encode("password@123")).thenReturn("encoded-password");
        when(coordinatorRepository.save(coordinator)).thenReturn(saved);
        when(coordinatorMapper.toResponse(saved)).thenReturn(response);

        CoordinatorResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
        assertThat(coordinator.getUser().getPasswordHash()).isEqualTo("encoded-password");
        assertThat(coordinator.getUser().getRoles()).containsExactly(Role.ROLE_COORDINATOR);
        assertThat(coordinator.getType()).isEqualTo(EmployeeType.COORDINATOR);
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        CreateCoordinatorRequest request = new CreateCoordinatorRequest(
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "COORD001",
                "password@123",
                ShiftEnum.MORNING
        );

        when(userRepository.existsByRegistration("COORD001")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void shouldRejectDuplicateEmail() {
        CreateCoordinatorRequest request = new CreateCoordinatorRequest(
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "COORD001",
                "password@123",
                ShiftEnum.MORNING
        );

        when(userRepository.existsByRegistration("COORD001")).thenReturn(false);
        when(employeeRepository.existsByEmail("coordinator@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldDeleteCoordinatorUser() {
        Coordinator coordinator = coordinator();

        when(coordinatorRepository.findById(coordinator.getId())).thenReturn(Optional.of(coordinator));

        service.delete(coordinator.getId());

        verify(userRepository).delete(coordinator.getUser());
    }

    @Test
    void shouldThrowWhenCoordinatorIsMissing() {
        UUID id = UUID.randomUUID();
        when(coordinatorRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Coordinator coordinator() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("COORD001");
        user.setPasswordHash("raw-password");
        user.setRoles(Set.of(Role.ROLE_COORDINATOR));

        Coordinator coordinator = new Coordinator();
        coordinator.setId(UUID.randomUUID());
        coordinator.setUser(user);
        coordinator.setName("Ana Silva");
        coordinator.setEmail("coordinator@dogvision.com");
        coordinator.setPhone("11987654321");
        coordinator.setShift(ShiftEnum.MORNING);
        coordinator.setType(EmployeeType.COORDINATOR);
        return coordinator;
    }

    private CoordinatorResponse coordinatorResponse() {
        return new CoordinatorResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "COORD001",
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "MORNING",
                EmployeeType.COORDINATOR
        );
    }
}
