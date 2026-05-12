package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.mapper.TrainerMapper;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.TrainerNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.Trainer;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.TrainerRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.TrainerService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final TrainerMapper trainerMapper;
    private final PasswordEncoder passwordEncoder;

    public TrainerServiceImpl(
            TrainerRepository trainerRepository,
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            TrainerMapper trainerMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.trainerRepository = trainerRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.trainerMapper = trainerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TrainerResponse getById(UUID id) {
        Trainer trainer = trainerRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer",id
                ));

        return trainerMapper.toResponse(trainer);
    }

    @Override
    public TrainerResponse getByRegistration(String registration) {
        Trainer trainer = trainerRepository.findByRegistration(registration)
                .orElseThrow(() -> new TrainerNotFoundException(registration));

        return trainerMapper.toResponse(trainer);
    }

    @Override
    public List<TrainerResponse> getAll() {
        return trainerRepository.findAllWithUser()
                .stream()
                .map(trainerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TrainerResponse save(CreateTrainerRequest dto) {
        validateEmployeeConflicts(dto.registration(), dto.email());

        Trainer trainer = trainerMapper.toEntity(dto);
        User user = trainer.getUser();
        user.setRoles(Set.of(Role.ROLE_TRAINER));
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        return trainerMapper.toResponse(trainerRepository.save(trainer));
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Trainer trainer = trainerRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer", id));

        userRepository.delete(trainer.getUser());
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
