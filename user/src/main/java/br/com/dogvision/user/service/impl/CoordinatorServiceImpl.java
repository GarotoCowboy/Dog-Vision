package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.mapper.CoordinatorMapper;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.infra.exception.CoordinatorNotFoundException;
import br.com.dogvision.user.infra.exception.MonitorNotFoundException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.security.SpringSecurityConfig;
import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.CoordinatorRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.CoordinatorService;
import br.com.dogvision.user.service.EmployeeCreationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CoordinatorServiceImpl implements CoordinatorService {
//
//    private final CoordinatorRepository coordinatorRepository;
//    private final UserRepository userRepository;
//    private final EmployeeCreationService employeeCreationService;
//    private final CoordinatorMapper coordinatorMapper;
//
//    public CoordinatorServiceImpl(
//            CoordinatorRepository coordinatorRepository,
//            UserRepository userRepository,
//            EmployeeCreationService employeeCreationService,
//            CoordinatorMapper coordinatorMapper
//    ) {
//        this.coordinatorRepository = coordinatorRepository;
//        this.userRepository = userRepository;
//        this.employeeCreationService = employeeCreationService;
//        this.coordinatorMapper = coordinatorMapper;
//    }
//
//    @Override
//    public CoordinatorResponse getById(UUID id) {
//        Coordinator coordinator = coordinatorRepository.findByIdWithEmployeeAndUser(id)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Coordinator",id
//                ));
//
//        return coordinatorMapper.toResponse(coordinator);
//    }
//
//    @Override
//    public CoordinatorResponse getByRegistration(String registration) {
//        Coordinator coordinator = coordinatorRepository.findByRegistration(registration)
//                .orElseThrow(() -> new CoordinatorNotFoundException(registration));
//
//        return coordinatorMapper.toResponse(coordinator);
//    }
//
//    @Override
//    public List<CoordinatorResponse> getAll() {
//        return coordinatorRepository.findAllWithEmployeeAndUser()
//                .stream()
//                .map(coordinatorMapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    @Transactional
//    public CoordinatorResponse save(CreateCoordinatorRequest dto) {
//
//        // 1) cria User + Employee
//        Employee employee = employeeCreationService.createEmployee(
//                dto.registration(),
//                dto.password(),
//                dto.email(),
//                dto.name(),
//                dto.cpf(),
//                dto.phone(),
//                EmployeeType.COORDINATOR,
//                Role.ROLE_COORDINATOR
//        );
//
//        // 2) cria Coordinator (subtipo)
//        Coordinator coordinator = new Coordinator();
//        coordinator.setEmployee(employee);
//        coordinator.setCodAdmin(dto.codAdmin());
//
//        Coordinator saved = coordinatorRepository.save(coordinator);
//
//        return coordinatorMapper.toResponse(saved);
//    }
//
//    @Override
//    @Transactional
//    public void delete(UUID id) {
//
//        Coordinator coordinator = coordinatorRepository.findByIdWithEmployeeAndUser(id)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Coordenador não encontrado"
//                ));
//
//        // Soft delete da conta
//        userRepository.delete(coordinator.getEmployee().getUser());
//    }

    private final CoordinatorRepository coordinatorRepository;
    private final UserRepository userRepository;
    private final CoordinatorMapper coordinatorMapper;
    private final PasswordEncoder passwordEncoder;

    public CoordinatorServiceImpl(
            CoordinatorRepository coordinatorRepository,
            UserRepository userRepository,
            CoordinatorMapper coordinatorMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.coordinatorRepository = coordinatorRepository;
        this.userRepository = userRepository;
        this.coordinatorMapper = coordinatorMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CoordinatorResponse getById(UUID id) {

        Coordinator coordinator = coordinatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator", id));

        return coordinatorMapper.toResponse(coordinator);
    }

    @Override
    public CoordinatorResponse getByRegistration(String registration) {
        Coordinator coordinator = coordinatorRepository.findByRegistration(registration)
                .orElseThrow(() -> new CoordinatorNotFoundException(registration));

        return coordinatorMapper.toResponse(coordinator);
    }

    @Override
    public List<CoordinatorResponse> getAll() {
        return coordinatorRepository.findAll()
                .stream()
                .map(coordinatorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CoordinatorResponse save(CreateCoordinatorRequest dto) {
        Coordinator coordinator = coordinatorMapper.toEntity(dto);
        User user = coordinator.getUser();
        user.setRoles(Set.of(Role.ROLE_COORDINATOR));


        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        Coordinator saved = coordinatorRepository.save(coordinator);

        return coordinatorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Coordinator coordinator = coordinatorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Coordenador não encontrado"
                ));

        userRepository.delete(coordinator.getUser());
    }

}
