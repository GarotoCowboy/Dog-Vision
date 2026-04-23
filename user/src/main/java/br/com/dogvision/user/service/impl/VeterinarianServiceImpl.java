package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.mapper.VeterinarianMapper;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.infra.exception.CpfAlreadyExistsException;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.infra.exception.VeterinarianCrmvAlreadyExistsException;
import br.com.dogvision.user.infra.exception.VeterinarianNotFoundException;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.model.Veterinarian;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.repository.VeterinarianRepository;
import br.com.dogvision.user.service.VeterinarianService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class VeterinarianServiceImpl implements VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final VeterinarianMapper veterinarianMapper;
    private final PasswordEncoder passwordEncoder;

    public VeterinarianServiceImpl(
            VeterinarianRepository veterinarianRepository,
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            VeterinarianMapper veterinarianMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.veterinarianRepository = veterinarianRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.veterinarianMapper = veterinarianMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public VeterinarianResponse getById(UUID id) {
        Veterinarian vet = veterinarianRepository.findByIdWithUser(id)
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
        return veterinarianRepository.findAllWithUser()
                .stream()
                .map(veterinarianMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public VeterinarianResponse save(CreateVeterinarianRequest dto) {
        validateEmployeeConflicts(dto.registration(), dto.email(), dto.cpf());

        if (veterinarianRepository.existsByCrmv(dto.crmv())) {
            throw new VeterinarianCrmvAlreadyExistsException(dto.crmv());
        }

        Veterinarian veterinarian = veterinarianMapper.toEntity(dto);
        User user = veterinarian.getUser();
        user.setRoles(Set.of(Role.ROLE_VETERINARIAN));
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        return veterinarianMapper.toResponse(veterinarianRepository.save(veterinarian));
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Veterinarian vet = veterinarianRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian", id));

        userRepository.delete(vet.getUser());
    }

    private void validateEmployeeConflicts(String registration, String email, String cpf) {
        if (userRepository.existsByRegistration(registration)) {
            throw new UserAlreadyExistsException(registration);
        }

        if (employeeRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        if (employeeRepository.existsByCpf(cpf)) {
            throw new CpfAlreadyExistsException(cpf);
        }
    }
}
