package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.create.CreateFirstCoordinatorRequest;
import br.com.dogvision.user.dto.events.UserCreatedEvent;
import br.com.dogvision.user.dto.mapper.CoordinatorMapper;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.infra.exception.CoordinatorNotFoundException;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.FirstCoordinatorAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.infra.rabbit.RabbitConfig;
import br.com.dogvision.user.model.Coordinator;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.CoordinatorRepository;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoordinatorServiceImplTest {

    @Mock
    private CoordinatorRepository coordinatorRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private CoordinatorMapper coordinatorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private CoordinatorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CoordinatorServiceImpl(
                coordinatorRepository,
                employeeRepository,
                userRepository,
                userService,
                coordinatorMapper,
                passwordEncoder,
                rabbitTemplate
        );
    }

    @Test
    void shouldReturnCoordinatorById() {
        Coordinator coordinator = coordinator();
        CoordinatorResponse response = coordinatorResponse();

        when(coordinatorRepository.findByIdWithUser(coordinator.getId())).thenReturn(Optional.of(coordinator));
        when(coordinatorMapper.toResponse(coordinator)).thenReturn(response);

        CoordinatorResponse result = service.getById(coordinator.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldSaveCoordinatorUsingUserService() {
        CreateCoordinatorRequest request = new CreateCoordinatorRequest(
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "COORD001",
                ShiftEnum.MORNING
        );
        Coordinator coordinator = coordinator();
        Coordinator saved = coordinator();
        CoordinatorResponse response = coordinatorResponse();
        User user = new User();
        user.setRegistration("COORD001");
        user.setRoles(Set.of(Role.ROLE_COORDINATOR));

        when(employeeRepository.existsByEmail("coordinator@dogvision.com")).thenReturn(false);
        when(userService.createAccount("COORD001", "coordinator@dogvision.com", "Ana Silva", Role.ROLE_COORDINATOR)).thenReturn(user);
        when(coordinatorMapper.toEntity(request)).thenReturn(coordinator);
        when(coordinatorRepository.save(coordinator)).thenReturn(saved);
        when(coordinatorMapper.toResponse(saved)).thenReturn(response);

        CoordinatorResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
        assertThat(coordinator.getUser()).isEqualTo(user);
        assertThat(coordinator.getType()).isEqualTo(EmployeeType.COORDINATOR);
    }

    @Test
    void shouldCreateFirstCoordinatorSuccessfully() {
        CreateFirstCoordinatorRequest request = new CreateFirstCoordinatorRequest(
                "root.coord@dogvision.com",
                "Root Coordinator",
                "11987654321",
                "ROOT001",
                "secretPassword@123",
                ShiftEnum.MORNING
        );
        CoordinatorResponse response = coordinatorResponse();

        when(coordinatorRepository.count()).thenReturn(0L);
        when(userRepository.existsByRegistration("ROOT001")).thenReturn(false);
        when(employeeRepository.existsByEmail("root.coord@dogvision.com")).thenReturn(false);
        when(passwordEncoder.encode("secretPassword@123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(coordinatorRepository.save(any(Coordinator.class))).thenAnswer(inv -> inv.getArgument(0));
        when(coordinatorMapper.toResponse(any(Coordinator.class))).thenReturn(response);

        CoordinatorResponse result = service.createFirstCoordinator(request);

        assertThat(result).isEqualTo(response);
        verify(rabbitTemplate).convertAndSend(eq(RabbitConfig.USER_EXCHANGE), eq(RabbitConfig.USER_CREATED_ROUTING_KEY), any(UserCreatedEvent.class));
    }

    @Test
    void shouldThrowConflictWhenFirstCoordinatorAlreadyExists() {
        CreateFirstCoordinatorRequest request = new CreateFirstCoordinatorRequest(
                "root.coord@dogvision.com",
                "Root Coordinator",
                "11987654321",
                "ROOT001",
                "secretPassword@123",
                ShiftEnum.MORNING
        );

        when(coordinatorRepository.count()).thenReturn(1L);

        assertThatThrownBy(() -> service.createFirstCoordinator(request))
                .isInstanceOf(FirstCoordinatorAlreadyExistsException.class)
                .satisfies(ex -> assertThat(((FirstCoordinatorAlreadyExistsException) ex).getStatus()).isEqualTo(HttpStatus.CONFLICT));

        verify(userRepository, never()).save(any());
        verify(coordinatorRepository, never()).save(any());
    }

    @Test
    void shouldRejectDuplicateEmail() {
        CreateCoordinatorRequest request = new CreateCoordinatorRequest(
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "COORD001",
                ShiftEnum.MORNING
        );

        when(employeeRepository.existsByEmail("coordinator@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldDeleteCoordinatorUser() {
        Coordinator coordinator = coordinator();
        when(coordinatorRepository.findByIdWithUser(coordinator.getId())).thenReturn(Optional.of(coordinator));

        service.delete(coordinator.getId());

        verify(userRepository).delete(coordinator.getUser());
    }

    @Test
    void shouldThrowWhenCoordinatorNotFound() {
        UUID id = UUID.randomUUID();
        when(coordinatorRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldThrowWhenCoordinatorRegistrationNotFound() {
        when(coordinatorRepository.findByRegistration("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByRegistration("missing"))
                .isInstanceOf(CoordinatorNotFoundException.class);
    }

    @Test
    void shouldListAllCoordinators() {
        Coordinator coordinator = coordinator();
        CoordinatorResponse response = coordinatorResponse();

        when(coordinatorRepository.findAllWithUser()).thenReturn(List.of(coordinator));
        when(coordinatorMapper.toResponse(coordinator)).thenReturn(response);

        List<CoordinatorResponse> result = service.getAll();

        assertThat(result).containsExactly(response);
    }

    private Coordinator coordinator() {
        Coordinator coordinator = new Coordinator();
        coordinator.setId(UUID.randomUUID());
        coordinator.setName("Ana Silva");
        coordinator.setEmail("coordinator@dogvision.com");
        coordinator.setPhone("11987654321");
        coordinator.setShift(ShiftEnum.MORNING);
        coordinator.setType(EmployeeType.COORDINATOR);

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("COORD001");
        user.setPasswordHash("hashed-password");
        user.setActive(true);
        user.setRoles(Set.of(Role.ROLE_COORDINATOR));
        coordinator.setUser(user);

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
                EmployeeType.COORDINATOR,
                true
        );
    }
}
