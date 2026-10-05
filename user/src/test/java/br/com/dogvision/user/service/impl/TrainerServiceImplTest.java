package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.mapper.TrainerMapper;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.TrainerNotFoundException;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.Trainer;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.TrainerRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainerMapper trainerMapper;

    private TrainerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TrainerServiceImpl(
                trainerRepository,
                employeeRepository,
                userRepository,
                userService,
                trainerMapper
        );
    }

    @Test
    void shouldReturnTrainerById() {
        Trainer trainer = trainer();
        TrainerResponse response = trainerResponse();

        when(trainerRepository.findByIdWithUser(trainer.getId())).thenReturn(Optional.of(trainer));
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = service.getById(trainer.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldReturnTrainerByRegistration() {
        Trainer trainer = trainer();
        TrainerResponse response = trainerResponse();

        when(trainerRepository.findByRegistration("TRAIN001")).thenReturn(Optional.of(trainer));
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = service.getByRegistration("TRAIN001");

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldSaveTrainerUsingUserService() {
        CreateTrainerRequest request = new CreateTrainerRequest(
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "TRAIN001",
                ShiftEnum.AFTERNOON,
                "Behavior"
        );
        Trainer trainer = trainer();
        TrainerResponse response = trainerResponse();
        User user = new User();
        user.setRegistration("TRAIN001");
        user.setRoles(Set.of(Role.ROLE_TRAINER));

        when(employeeRepository.existsByEmail("trainer@dogvision.com")).thenReturn(false);
        when(userService.createAccount("TRAIN001", "trainer@dogvision.com", "Pedro Almeida", Role.ROLE_TRAINER)).thenReturn(user);
        when(trainerMapper.toEntity(request)).thenReturn(trainer);
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
        assertThat(trainer.getUser()).isEqualTo(user);
    }

    @Test
    void shouldRejectTrainerWhenEmailAlreadyExists() {
        CreateTrainerRequest request = new CreateTrainerRequest(
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "TRAIN001",
                ShiftEnum.AFTERNOON,
                "Behavior"
        );

        when(employeeRepository.existsByEmail("trainer@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldDeleteTrainerUser() {
        Trainer trainer = trainer();
        when(trainerRepository.findByIdWithUser(trainer.getId())).thenReturn(Optional.of(trainer));

        service.delete(trainer.getId());

        verify(userRepository).delete(trainer.getUser());
    }

    @Test
    void shouldThrowWhenTrainerNotFound() {
        UUID id = UUID.randomUUID();
        when(trainerRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldThrowWhenTrainerRegistrationNotFound() {
        when(trainerRepository.findByRegistration("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByRegistration("missing"))
                .isInstanceOf(TrainerNotFoundException.class);
    }

    @Test
    void shouldListAllTrainers() {
        Trainer trainer = trainer();
        TrainerResponse response = trainerResponse();

        when(trainerRepository.findAllWithUser()).thenReturn(List.of(trainer));
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        List<TrainerResponse> result = service.getAll();

        assertThat(result).containsExactly(response);
    }

    private Trainer trainer() {
        Trainer trainer = new Trainer();
        trainer.setId(UUID.randomUUID());
        trainer.setName("Pedro Almeida");
        trainer.setEmail("trainer@dogvision.com");
        trainer.setPhone("11987654321");
        trainer.setShift(ShiftEnum.AFTERNOON);
        trainer.setAreaOfExpertise("Behavior");

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("TRAIN001");
        user.setPasswordHash("hashed-password");
        user.setActive(true);
        user.setRoles(Set.of(Role.ROLE_TRAINER));
        trainer.setUser(user);

        return trainer;
    }

    private TrainerResponse trainerResponse() {
        return new TrainerResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "TRAIN001",
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "AFTERNOON",
                EmployeeType.TRAINER,
                "Behavior",
                true
        );
    }
}
