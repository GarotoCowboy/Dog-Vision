package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.mapper.VeterinarianMapper;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.VeterinarianNotFoundException;
import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.repository.VeterinarianRepository;
import br.com.dogvision.user.service.EmployeeCreationService;
import br.com.dogvision.user.service.VeterinarianService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class VeterinarianServiceImpl implements VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;
    private final UserRepository userRepository;
    private final EmployeeCreationService employeeCreationService;
    private final VeterinarianMapper veterinarianMapper;

    public VeterinarianServiceImpl(
            VeterinarianRepository veterinarianRepository,
            UserRepository userRepository,
            EmployeeCreationService employeeCreationService,
            VeterinarianMapper veterinarianMapper
    ) {
        this.veterinarianRepository = veterinarianRepository;
        this.userRepository = userRepository;
        this.employeeCreationService = employeeCreationService;
        this.veterinarianMapper = veterinarianMapper;
    }

    @Override
    public VeterinarianResponse getById(UUID id) {
        Veterinarian vet = veterinarianRepository.findByIdWithEmployeeAndUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veterinarian",id
                ));
        return veterinarianMapper.toResponse(vet);
    }

    @Override
    public VeterinarianResponse getByRegistration(String registration) {
        Veterinarian veterinarian = veterinarianRepository.findByRegistration(registration)
                .orElseThrow(() -> new VeterinarianNotFoundException(registration));

        return veterinarianMapper.toResponse(veterinarian);
    }


    @Override
    public List<VeterinarianResponse> getAll() {
        return veterinarianRepository.findAllWithEmployeeAndUser()
                .stream()
                .map(veterinarianMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public VeterinarianResponse save(CreateVeterinarianRequest dto) {

        // Subtipo unique
        if (veterinarianRepository.existsByCrmv(dto.crmv())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CRMV já existe");
        }

        // 1) cria User + Employee (valida registration/email/cpf)
        Employee employee = employeeCreationService.createEmployee(
                dto.registration(),
                dto.password(),
                dto.email(),
                dto.name(),
                dto.cpf(),
                dto.phone(),
                EmployeeType.VETERINARIAN,
                Role.ROLE_VETERINARIAN
        );

        // 2) cria o Veterinarian (PK compartilhada via @MapsId)
        Veterinarian vet = new Veterinarian();
        vet.setEmployee(employee);
        vet.setCrmv(dto.crmv());
        vet.setAreaOfExpertise(dto.areaOfExpertise());

        Veterinarian saved = veterinarianRepository.save(vet);

        // dentro da transação, o mapper acessa employee/user sem LazyException
        return veterinarianMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Veterinarian vet = veterinarianRepository.findByIdWithEmployeeAndUser(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Veterinário não encontrado"
                ));

        // "deletar veterinário" = desativar a conta (soft delete)
        userRepository.delete(vet.getEmployee().getUser());

        // (opcional) se quiser remover só o subtipo hard delete:
        // veterinarianRepository.delete(vet);
    }
}
