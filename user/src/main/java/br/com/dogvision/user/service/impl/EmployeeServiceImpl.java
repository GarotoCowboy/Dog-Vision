package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateEmployeeRequest;
import br.com.dogvision.user.dto.mapper.EmployeeMapper;
import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.EmployeeService;
import br.com.dogvision.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmployeeMapper mapper;

    @Override
    public EmployeeResponse getById(UUID id) {
        Employee e = employeeRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        return mapper.toResponse(e);
    }

    @Override
    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAllWithUser()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EmployeeResponse save(CreateEmployeeRequest dto) {
        if (employeeRepository.existsByEmail(dto.email()))
            throw new EmailAlreadyExistsException(dto.email());

        Role role = resolveRole(dto.type());
        User user = userService.createAccount(dto.registration(), dto.email(), dto.name(), role);

        Employee employee = mapper.toEntity(dto);
        employee.setUser(user);

        return mapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse update(UUID id, UpdateEmployeeRequest dto) {
        Employee employee = employeeRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        if (dto.email() != null && !dto.email().equals(employee.getEmail())) {
            if (employeeRepository.existsByEmail(dto.email()))
                throw new EmailAlreadyExistsException(dto.email());
        }

        mapper.updateFromDto(dto, employee);

        return mapper.toResponse(employee);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Employee employee = employeeRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        userRepository.delete(employee.getUser());
    }

    private Role resolveRole(EmployeeType type) {
        return switch (type) {
            case COLLABORATOR -> Role.ROLE_COLLABORATOR;
            case TRAINER -> Role.ROLE_TRAINER;
            case COORDINATOR -> Role.ROLE_COORDINATOR;
            case VETERINARIAN -> Role.ROLE_VETERINARIAN;
        };
    }
}
