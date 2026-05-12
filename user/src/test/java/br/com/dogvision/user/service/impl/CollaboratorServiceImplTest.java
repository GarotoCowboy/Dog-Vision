package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCollaboratorRequest;
import br.com.dogvision.user.dto.mapper.CollaboratorMapper;
import br.com.dogvision.user.dto.response.CollaboratorResponse;
import br.com.dogvision.user.infra.exception.CollaboratorNotFoundException;
import br.com.dogvision.user.model.Collaborator;
import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.CollaboratorRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.EmployeeCreationService;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollaboratorServiceImplTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeCreationService employeeCreationService;

    @Mock
    private CollaboratorMapper collaboratorMapper;

    private CollaboratorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CollaboratorServiceImpl(
                collaboratorRepository,
                userRepository,
                employeeCreationService,
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
    void shouldCreateCollaboratorUsingEmployeeCreationService() {
        CreateCollaboratorRequest request = new CreateCollaboratorRequest(
                "collaborator@dogvision.com",
                "Carlos Souza",
                "11987654321",
                "COL001",
                "password@123",
                ShiftEnum.MORNING
        );
        Employee employee = new Employee();
        CollaboratorResponse response = collaboratorResponse();

        when(employeeCreationService.createEmployee(
                eq("COL001"),
                eq("password@123"),
                eq("collaborator@dogvision.com"),
                eq("Carlos Souza"),
                eq("11987654321"),
                eq(ShiftEnum.MORNING),
                eq(EmployeeType.COLLABORATOR),
                eq(Role.ROLE_COLLABORATOR)
        )).thenReturn(employee);
        when(collaboratorMapper.toResponse(employee)).thenReturn(response);

        CollaboratorResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
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
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setRegistration("COL001");

        Collaborator collaborator = new Collaborator();
        collaborator.setId(UUID.randomUUID());
        collaborator.setUser(user);
        collaborator.setName("Carlos Souza");
        collaborator.setShift(ShiftEnum.MORNING);
        return collaborator;
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
                "MORNING"
        );
    }
}
