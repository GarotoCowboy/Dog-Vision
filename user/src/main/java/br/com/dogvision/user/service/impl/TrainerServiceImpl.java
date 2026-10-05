package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.mapper.TrainerMapper;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.TrainerNotFoundException;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.Trainer;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.TrainerRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.TrainerService;
import br.com.dogvision.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TrainerMapper trainerMapper;

    @Override
    public TrainerResponse getById(UUID id) {
        Trainer trainer = trainerRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer", id
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
        if (employeeRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = userService.createAccount(dto.registration(), dto.email(), dto.name(), Role.ROLE_TRAINER);

        Trainer trainer = trainerMapper.toEntity(dto);
        trainer.setUser(user);

        return trainerMapper.toResponse(trainerRepository.save(trainer));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Trainer trainer = trainerRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer", id));

        userRepository.delete(trainer.getUser());
    }
}
