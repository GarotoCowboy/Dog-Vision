package br.com.dogvision.user.service.impl;
import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.mapper.VeterinarianMapper;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.infra.exception.*;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.model.Veterinarian;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.repository.VeterinarianRepository;
import br.com.dogvision.user.service.UserService;
import br.com.dogvision.user.service.VeterinarianService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class VeterinarianServiceImpl implements VeterinarianService {
    private final VeterinarianRepository veterinarianRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final VeterinarianMapper veterinarianMapper;
    @Override
    public VeterinarianResponse getById(UUID id) {
        Veterinarian vet = veterinarianRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian", id));
        return veterinarianMapper.toResponse(vet);
    }
    @Override
    public VeterinarianResponse getByRegistration(String registration) {
        Veterinarian vet = veterinarianRepository.findByRegistration(registration)
                .orElseThrow(() -> new VeterinarianNotFoundException(registration));
        return veterinarianMapper.toResponse(vet);
    }

        @Override
    @Transactional
    public VeterinarianResponse save(CreateVeterinarianRequest dto) {
        if (employeeRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }
        if (veterinarianRepository.existsByCrmv(dto.crmv())) {
            throw new VeterinarianCrmvAlreadyExistsException(dto.crmv());
        }
        User user = userService.createAccount(dto.registration(), dto.email(), dto.name(), Role.ROLE_VETERINARIAN);
        Veterinarian veterinarian = veterinarianMapper.toEntity(dto);
        veterinarian.setUser(user);
        return veterinarianMapper.toResponse(veterinarianRepository.save(veterinarian));
    }
    @Override
    @Transactional
    public void delete(UUID id) {
        Veterinarian vet = veterinarianRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian", id));
        userRepository.delete(vet.getUser());
    }
    @Override
    public List<VeterinarianResponse> getAll() {
        return veterinarianRepository.findAllWithUser()
                .stream()
                .map(veterinarianMapper::toResponse)
                .toList();
    }
}
