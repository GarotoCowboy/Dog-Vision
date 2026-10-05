package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.create.CreateFirstCoordinatorRequest;
import br.com.dogvision.user.dto.events.UserCreatedEvent;
import br.com.dogvision.user.dto.mapper.CoordinatorMapper;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.infra.exception.CoordinatorNotFoundException;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.FirstCoordinatorAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.infra.rabbit.RabbitConfig;
import br.com.dogvision.user.model.Coordinator;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.CoordinatorRepository;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.CoordinatorService;
import br.com.dogvision.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final UserService userService;
    private final CoordinatorMapper coordinatorMapper;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public CoordinatorResponse getById(UUID id) {
        Coordinator coordinator = coordinatorRepository.findByIdWithUser(id)
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
        return coordinatorRepository.findAllWithUser()
                .stream()
                .map(coordinatorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CoordinatorResponse save(CreateCoordinatorRequest dto) {
        if (employeeRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = userService.createAccount(dto.registration(), dto.email(), dto.name(), Role.ROLE_COORDINATOR);

        Coordinator coordinator = coordinatorMapper.toEntity(dto);
        coordinator.setUser(user);
        coordinator.setType(EmployeeType.COORDINATOR);

        Coordinator saved = coordinatorRepository.save(coordinator);

        return coordinatorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CoordinatorResponse createFirstCoordinator(CreateFirstCoordinatorRequest dto) {
        if (coordinatorRepository.count() > 0) {
            throw new FirstCoordinatorAlreadyExistsException();
        }

        if (userRepository.existsByRegistration(dto.registration())) {
            throw new UserAlreadyExistsException(dto.registration());
        }

        if (employeeRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setRegistration(dto.registration());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(Role.ROLE_COORDINATOR));
        User savedUser = userRepository.save(user);

        Coordinator coordinator = new Coordinator();
        coordinator.setUser(savedUser);
        coordinator.setEmail(dto.email());
        coordinator.setName(dto.name());
        coordinator.setPhone(dto.phone());
        coordinator.setShift(dto.shift());
        coordinator.setType(EmployeeType.COORDINATOR);

        Coordinator savedCoordinator = coordinatorRepository.save(coordinator);

        UserCreatedEvent event = new UserCreatedEvent(
                savedCoordinator.getName(),
                savedCoordinator.getEmail(),
                savedUser.getRegistration(),
                dto.password()
        );
        rabbitTemplate.convertAndSend(RabbitConfig.USER_EXCHANGE, RabbitConfig.USER_CREATED_ROUTING_KEY, event);

        return coordinatorMapper.toResponse(savedCoordinator);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Coordinator coordinator = coordinatorRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator", id));

        userRepository.delete(coordinator.getUser());
    }
}
