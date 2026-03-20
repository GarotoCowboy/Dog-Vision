package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.mapper.TrainerMapper;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.infra.exception.MonitorNotFoundException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.TrainerNotFoundException;
import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.TrainerRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.EmployeeCreationService;
import br.com.dogvision.user.service.TrainerService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final EmployeeCreationService employeeCreationService;
    private final TrainerMapper trainerMapper;

    public TrainerServiceImpl(
            TrainerRepository trainerRepository,
            UserRepository userRepository,
            EmployeeCreationService employeeCreationService,
            TrainerMapper trainerMapper
    ) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.employeeCreationService = employeeCreationService;
        this.trainerMapper = trainerMapper;
    }

    @Override
    public TrainerResponse getById(UUID id) {
        Trainer trainer = trainerRepository.findByIdWithEmployeeAndUser(id)
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
        return trainerRepository.findAllWithEmployeeAndUser()
                .stream()
                .map(trainerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TrainerResponse save(CreateTrainerRequest dto) {

        // 1) cria User + Employee
        Employee employee = employeeCreationService.createEmployee(
                dto.registration(),
                dto.password(),
                dto.email(),
                dto.name(),
                dto.cpf(),
                dto.phone(),
                EmployeeType.TRAINER,
                Role.ROLE_TRAINER
        );

        // 2) cria Trainer (subtipo)
        Trainer trainer = new Trainer();
        trainer.setEmployee(employee);
        trainer.setAreaOfExpertise(dto.areaOfExpertise());

        Trainer saved = trainerRepository.save(trainer);

        return trainerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Trainer trainer = trainerRepository.findByIdWithEmployeeAndUser(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trainer não encontrado"
                ));

        // Soft delete da conta
        userRepository.delete(trainer.getEmployee().getUser());
    }
}
