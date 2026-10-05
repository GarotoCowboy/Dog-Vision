package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCollaboratorRequest;
import br.com.dogvision.user.dto.mapper.CollaboratorMapper;
import br.com.dogvision.user.dto.response.CollaboratorResponse;
import br.com.dogvision.user.infra.exception.CollaboratorNotFoundException;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.CollaboratorRepository;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.CollaboratorService;
import br.com.dogvision.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CollaboratorMapper collaboratorMapper;

    public CollaboratorServiceImpl(
            CollaboratorRepository collaboratorRepository,
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            UserService userService,
            CollaboratorMapper collaboratorMapper
    ) {
        this.collaboratorRepository = collaboratorRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.collaboratorMapper = collaboratorMapper;
    }

    @Override
    public CollaboratorResponse getById(UUID id) {
        Collaborator collaborator = collaboratorRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Collaborator", id
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
        if (employeeRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = userService.createAccount(
                dto.registration(),
                dto.email(),
                dto.name(),
                Role.ROLE_COLLABORATOR
        );

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setEmail(dto.email());
        employee.setName(dto.name());
        employee.setPhone(dto.phone());
        employee.setShift(dto.shift());
        employee.setType(EmployeeType.COLLABORATOR);

        return collaboratorMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Collaborator collaborator = collaboratorRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Collaborator not found"
                ));

        userRepository.delete(collaborator.getUser());
    }
}
