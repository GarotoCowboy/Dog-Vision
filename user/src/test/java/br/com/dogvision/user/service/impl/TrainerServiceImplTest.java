package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.mapper.TrainerMapper;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.Trainer;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.TrainerRepository;
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
    private TrainerMapper trainerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private TrainerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TrainerServiceImpl(trainerRepository, employeeRepository, userRepository, trainerMapper, passwordEncoder);
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
    void shouldSaveTrainerWithEncodedPasswordAndRole() {
        CreateTrainerRequest request = new CreateTrainerRequest(
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "TRAIN001",
                "password@123",
                ShiftEnum.AFTERNOON,
                "Behavior"
        );
        Trainer trainer = trainer();
        TrainerResponse response = trainerResponse();

        trainer.getUser().setPasswordHash("password@123");

        when(userRepository.existsByRegistration("TRAIN001")).thenReturn(false);
        when(employeeRepository.existsByEmail("trainer@dogvision.com")).thenReturn(false);
        when(trainerMapper.toEntity(request)).thenReturn(trainer);
        when(passwordEncoder.encode("password@123")).thenReturn("encoded-password");
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
        assertThat(trainer.getUser().getRoles()).containsExactly(Role.ROLE_TRAINER);
        assertThat(trainer.getUser().getPasswordHash()).isEqualTo("encoded-password");
    }

    @Test
    void shouldRejectTrainerWhenRegistrationAlreadyExists() {
        CreateTrainerRequest request = new CreateTrainerRequest(
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "TRAIN001",
                "password@123",
                ShiftEnum.AFTERNOON,
                "Behavior"
        );

        when(userRepository.existsByRegistration("TRAIN001")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void shouldRejectTrainerWhenEmailAlreadyExists() {
        CreateTrainerRequest request = new CreateTrainerRequest(
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "TRAIN001",
                "password@123",
                ShiftEnum.AFTERNOON,
                "Behavior"
        );

        when(userRepository.existsByRegistration("TRAIN001")).thenReturn(false);
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
    void shouldThrowWhenTrainerIdDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(trainerRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Trainer trainer() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("TRAIN001");
        user.setPasswordHash("password@123");
        user.setRoles(Set.of(Role.ROLE_TRAINER));

        Trainer trainer = new Trainer();
        trainer.setId(UUID.randomUUID());
        trainer.setUser(user);
        trainer.setName("Pedro Almeida");
        trainer.setEmail("trainer@dogvision.com");
        trainer.setPhone("11987654321");
        trainer.setShift(ShiftEnum.AFTERNOON);
        trainer.setType(EmployeeType.TRAINER);
        trainer.setAreaOfExpertise("Behavior");
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
                "Behavior"
        );
    }
}
