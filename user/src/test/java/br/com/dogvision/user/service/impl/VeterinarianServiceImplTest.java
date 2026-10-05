package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.mapper.VeterinarianMapper;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.infra.exception.*;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.model.Veterinarian;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.repository.VeterinarianRepository;
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
class VeterinarianServiceImplTest {

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private VeterinarianMapper veterinarianMapper;

    private VeterinarianServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VeterinarianServiceImpl(
                veterinarianRepository,
                employeeRepository,
                userRepository,
                userService,
                veterinarianMapper
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
    void shouldReturnVeterinarianByRegistration() {
        Veterinarian veterinarian = veterinarian();
        VeterinarianResponse response = veterinarianResponse();

        when(veterinarianRepository.findByRegistration("VET001")).thenReturn(Optional.of(veterinarian));
        when(veterinarianMapper.toResponse(veterinarian)).thenReturn(response);

        VeterinarianResponse result = service.getByRegistration("VET001");

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldSaveVeterinarianUsingUserService() {
        CreateVeterinarianRequest request = new CreateVeterinarianRequest(
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "VET001",
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );
        Veterinarian veterinarian = veterinarian();
        VeterinarianResponse response = veterinarianResponse();
        User user = new User();
        user.setRegistration("VET001");
        user.setRoles(Set.of(Role.ROLE_VETERINARIAN));

        when(employeeRepository.existsByEmail("vet@dogvision.com")).thenReturn(false);
        when(veterinarianRepository.existsByCrmv("SP-12345")).thenReturn(false);
        when(userService.createAccount("VET001", "vet@dogvision.com", "Anna Costa", Role.ROLE_VETERINARIAN)).thenReturn(user);
        when(veterinarianMapper.toEntity(request)).thenReturn(veterinarian);
        when(veterinarianRepository.save(veterinarian)).thenReturn(veterinarian);
        when(veterinarianMapper.toResponse(veterinarian)).thenReturn(response);

        VeterinarianResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
        assertThat(veterinarian.getUser()).isEqualTo(user);
    }

    @Test
    void shouldRejectVeterinarianWhenEmailAlreadyExists() {
        CreateVeterinarianRequest request = new CreateVeterinarianRequest(
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "VET001",
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );

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
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );

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
    void shouldThrowWhenVeterinarianNotFound() {
        UUID id = UUID.randomUUID();
        when(veterinarianRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldThrowWhenVeterinarianRegistrationNotFound() {
        when(veterinarianRepository.findByRegistration("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByRegistration("missing"))
                .isInstanceOf(VeterinarianNotFoundException.class);
    }

    @Test
    void shouldListAllVeterinarians() {
        Veterinarian veterinarian = veterinarian();
        VeterinarianResponse response = veterinarianResponse();

        when(veterinarianRepository.findAllWithUser()).thenReturn(List.of(veterinarian));
        when(veterinarianMapper.toResponse(veterinarian)).thenReturn(response);

        List<VeterinarianResponse> result = service.getAll();

        assertThat(result).containsExactly(response);
    }

    private Veterinarian veterinarian() {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(UUID.randomUUID());
        veterinarian.setName("Anna Costa");
        veterinarian.setEmail("vet@dogvision.com");
        veterinarian.setPhone("11987654321");
        veterinarian.setShift(ShiftEnum.MORNING);
        veterinarian.setCrmv("SP-12345");
        veterinarian.setAreaOfExpertise("General practice");

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("VET001");
        user.setPasswordHash("hashed-password");
        user.setActive(true);
        user.setRoles(Set.of(Role.ROLE_VETERINARIAN));
        veterinarian.setUser(user);

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
                "General practice",
                true
        );
    }
}
