package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCollaboratorRequest;
import br.com.dogvision.user.dto.mapper.CollaboratorMapper;
import br.com.dogvision.user.dto.response.CollaboratorResponse;
import br.com.dogvision.user.infra.exception.CollaboratorNotFoundException;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.model.Collaborator;
import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.CollaboratorRepository;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollaboratorServiceImplTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private CollaboratorMapper collaboratorMapper;

    private CollaboratorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CollaboratorServiceImpl(
                collaboratorRepository,
                employeeRepository,
                userRepository,
                userService,
                collaboratorMapper
        );
    }

    @Test
    void shouldReturnCollaboratorByRegistration() {
        Collaborator collaborator = collaborator();
        CollaboratorResponse response = collaboratorResponse();

        when(collaboratorRepository.findByRegistration("COL001")).thenReturn(Optional.of(collaborator));
        when(collaboratorMapper.toResponse(collaborator)).thenReturn(response);

        CollaboratorResponse result = service.getByRegistration("COL001");

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldCreateCollaboratorUsingUserService() {
        CreateCollaboratorRequest request = new CreateCollaboratorRequest(
                "collaborator@dogvision.com",
                "Carlos Souza",
                "11987654321",
                "COL001",
                ShiftEnum.MORNING
        );
        User user = new User();
        user.setRegistration("COL001");
        Employee employee = new Employee();
        CollaboratorResponse response = collaboratorResponse();

        when(employeeRepository.existsByEmail("collaborator@dogvision.com")).thenReturn(false);
        when(userService.createAccount("COL001", "collaborator@dogvision.com", "Carlos Souza", Role.ROLE_COLLABORATOR)).thenReturn(user);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(collaboratorMapper.toResponse(employee)).thenReturn(response);

        CollaboratorResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldThrowWhenCollaboratorEmailAlreadyExists() {
        CreateCollaboratorRequest request = new CreateCollaboratorRequest(
                "collaborator@dogvision.com",
                "Carlos Souza",
                "11987654321",
                "COL001",
                ShiftEnum.MORNING
        );

        when(employeeRepository.existsByEmail("collaborator@dogvision.com")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldDeleteCollaboratorUser() {
        Collaborator collaborator = collaborator();

        when(collaboratorRepository.findByIdWithUser(collaborator.getId())).thenReturn(Optional.of(collaborator));

        service.delete(collaborator.getId());

        verify(userRepository).delete(collaborator.getUser());
    }

    @Test
    void shouldThrowWhenCollaboratorRegistrationDoesNotExist() {
        when(collaboratorRepository.findByRegistration("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByRegistration("missing"))
                .isInstanceOf(CollaboratorNotFoundException.class);
    }

    @Test
    void shouldThrowWhenDeletingUnknownCollaborator() {
        UUID id = UUID.randomUUID();
        when(collaboratorRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ResponseStatusException.class);
    }

    private Collaborator collaborator() {
        Collaborator c = new Collaborator();
        c.setId(UUID.randomUUID());
        User u = new User();
        u.setRegistration("COL001");
        c.setUser(u);
        return c;
    }

    private CollaboratorResponse collaboratorResponse() {
        return new CollaboratorResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "COL001",
                "collaborator@dogvision.com",
                "Carlos Souza",
                "11987654321",
                EmployeeType.COLLABORATOR,
                "MORNING",
                true
        );
    }
}
