package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.mapper.VeterinarianMapper;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.infra.exception.VeterinarianCrmvAlreadyExistsException;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.model.Veterinarian;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.repository.VeterinarianRepository;
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
class VeterinarianServiceImplTest {

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VeterinarianMapper veterinarianMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private VeterinarianServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VeterinarianServiceImpl(
                veterinarianRepository,
                employeeRepository,
                userRepository,
                veterinarianMapper,
                passwordEncoder
        );
    }

    @Test
    void shouldReturnVeterinarianById() {
        Veterinarian veterinarian = veterinarian();
        VeterinarianResponse response = veterinarianResponse();

        when(veterinarianRepository.findByIdWithUser(veterinarian.getId())).thenReturn(Optional.of(veterinarian));
        when(veterinarianMapper.toResponse(veterinarian)).thenReturn(response);

        VeterinarianResponse result = service.getById(veterinarian.getId());

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldSaveVeterinarianWithEncodedPasswordAndRole() {
        CreateVeterinarianRequest request = new CreateVeterinarianRequest(
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "VET001",
                "password@123",
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );
        Veterinarian veterinarian = veterinarian();
        VeterinarianResponse response = veterinarianResponse();

        veterinarian.getUser().setPasswordHash("password@123");

        when(userRepository.existsByRegistration("VET001")).thenReturn(false);
        when(employeeRepository.existsByEmail("vet@dogvision.com")).thenReturn(false);
        when(veterinarianRepository.existsByCrmv("SP-12345")).thenReturn(false);
        when(veterinarianMapper.toEntity(request)).thenReturn(veterinarian);
        when(passwordEncoder.encode("password@123")).thenReturn("encoded-password");
        when(veterinarianRepository.save(veterinarian)).thenReturn(veterinarian);
        when(veterinarianMapper.toResponse(veterinarian)).thenReturn(response);

        VeterinarianResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
        assertThat(veterinarian.getUser().getRoles()).containsExactly(Role.ROLE_VETERINARIAN);
        assertThat(veterinarian.getUser().getPasswordHash()).isEqualTo("encoded-password");
    }

    @Test
    void shouldRejectVeterinarianWhenRegistrationAlreadyExists() {
        CreateVeterinarianRequest request = new CreateVeterinarianRequest(
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "VET001",
                "password@123",
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );

        when(userRepository.existsByRegistration("VET001")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void shouldRejectVeterinarianWhenEmailAlreadyExists() {
        CreateVeterinarianRequest request = new CreateVeterinarianRequest(
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "VET001",
                "password@123",
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );

        when(userRepository.existsByRegistration("VET001")).thenReturn(false);
        when(employeeRepository.existsByEmail("vet@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldRejectVeterinarianWhenCrmvAlreadyExists() {
        CreateVeterinarianRequest request = new CreateVeterinarianRequest(
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "VET001",
                "password@123",
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );

        when(userRepository.existsByRegistration("VET001")).thenReturn(false);
        when(employeeRepository.existsByEmail("vet@dogvision.com")).thenReturn(false);
        when(veterinarianRepository.existsByCrmv("SP-12345")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(VeterinarianCrmvAlreadyExistsException.class);
    }

    @Test
    void shouldDeleteVeterinarianUser() {
        Veterinarian veterinarian = veterinarian();

        when(veterinarianRepository.findByIdWithUser(veterinarian.getId())).thenReturn(Optional.of(veterinarian));

        service.delete(veterinarian.getId());

        verify(userRepository).delete(veterinarian.getUser());
    }

    @Test
    void shouldThrowWhenVeterinarianIdDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(veterinarianRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Veterinarian veterinarian() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("VET001");
        user.setPasswordHash("password@123");
        user.setRoles(Set.of(Role.ROLE_VETERINARIAN));

        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(UUID.randomUUID());
        veterinarian.setUser(user);
        veterinarian.setName("Anna Costa");
        veterinarian.setEmail("vet@dogvision.com");
        veterinarian.setPhone("11987654321");
        veterinarian.setShift(ShiftEnum.MORNING);
        veterinarian.setType(EmployeeType.VETERINARIAN);
        veterinarian.setCrmv("SP-12345");
        veterinarian.setAreaOfExpertise("General practice");
        return veterinarian;
    }

    private VeterinarianResponse veterinarianResponse() {
        return new VeterinarianResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "VET001",
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "MORNING",
                EmployeeType.VETERINARIAN,
                "SP-12345",
                "General practice"
        );
    }
}
