package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCollaboratorRequest;
import br.com.dogvision.user.dto.mapper.CollaboratorMapper;
import br.com.dogvision.user.dto.response.CollaboratorResponse;
import br.com.dogvision.user.infra.exception.CollaboratorNotFoundException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.CollaboratorRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.EmployeeCreationService;
import br.com.dogvision.user.service.CollaboratorService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final UserRepository userRepository;
    private final EmployeeCreationService employeeCreationService;
    private final CollaboratorMapper collaboratorMapper;

    public CollaboratorServiceImpl(
            CollaboratorRepository collaboratorRepository,
            UserRepository userRepository,
            EmployeeCreationService employeeCreationService,
            CollaboratorMapper collaboratorMapper
    ) {
        this.collaboratorRepository = collaboratorRepository;
        this.userRepository = userRepository;
        this.employeeCreationService = employeeCreationService;
        this.collaboratorMapper = collaboratorMapper;
    }

    @Override
    public CollaboratorResponse getById(UUID id) {
        Collaborator collaborator = collaboratorRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Collaborator",id
                ));

        return collaboratorMapper.toResponse(collaborator);
    }

    @Override
    public List<CollaboratorResponse> getAll() {
        return collaboratorRepository.findAllWithUser()
                .stream()
                .map(collaboratorMapper::toResponse)
                .toList();
    }

    @Override
    public CollaboratorResponse getByRegistration(String registration) {
        Collaborator collaborator = collaboratorRepository.findByRegistration(registration)
                .orElseThrow(() -> new CollaboratorNotFoundException(registration));

        return collaboratorMapper.toResponse(collaborator);
    }

    @Override
    @Transactional
    public CollaboratorResponse save(CreateCollaboratorRequest dto) {

        // 1) Create User + Employee
        Employee employee = employeeCreationService.createEmployee(
                dto.registration(),
                dto.password(),
                dto.email(),
                dto.name(),
                dto.phone(),
                dto.shift(),
                EmployeeType.COLLABORATOR,
                Role.ROLE_COLLABORATOR
        );

        return collaboratorMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Collaborator collaborator = collaboratorRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Collaborator not found"
                ));

        // Soft delete the account
        userRepository.delete(collaborator.getUser());
    }
}

