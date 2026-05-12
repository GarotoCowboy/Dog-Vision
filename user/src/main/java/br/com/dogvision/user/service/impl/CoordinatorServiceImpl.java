package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.mapper.CoordinatorMapper;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.infra.exception.CoordinatorNotFoundException;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.model.Coordinator;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.CoordinatorRepository;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.CoordinatorService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CoordinatorServiceImpl implements CoordinatorService {

    private final CoordinatorRepository coordinatorRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final CoordinatorMapper coordinatorMapper;
    private final PasswordEncoder passwordEncoder;

//    public CoordinatorServiceImpl(
//            CoordinatorRepository coordinatorRepository,
//            EmployeeRepository employeeRepository,
//            UserRepository userRepository,
//            CoordinatorMapper coordinatorMapper,
//            PasswordEncoder passwordEncoder
//    ) {
//        this.coordinatorRepository = coordinatorRepository;
//        this.employeeRepository = employeeRepository;
//        this.userRepository = userRepository;
//        this.coordinatorMapper = coordinatorMapper;
//        this.passwordEncoder = passwordEncoder;
//    }

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
        validateEmployeeConflicts(dto.registration(), dto.email());

        Coordinator coordinator = coordinatorMapper.toEntity(dto);
        User user = coordinator.getUser();
        user.setRoles(Set.of(Role.ROLE_COORDINATOR));


        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        coordinator.setType(EmployeeType.COORDINATOR);

        Coordinator saved = coordinatorRepository.save(coordinator);

        return coordinatorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Coordinator coordinator = coordinatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator", id));

        userRepository.delete(coordinator.getUser());
    }

    private void validateEmployeeConflicts(String registration, String email) {
        if (userRepository.existsByRegistration(registration)) {
            throw new UserAlreadyExistsException(registration);
        }

        if (employeeRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

}


