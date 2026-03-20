package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateEmployeeRequest;
import br.com.dogvision.user.dto.mapper.EmployeeMapper;
import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.infra.exception.CpfAlreadyExistsException;
import br.com.dogvision.user.infra.exception.EmailAlreadyExistsException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.EmployeeService;
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
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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

        if (userRepository.existsByRegistration(dto.registration()))
            throw new UserAlreadyExistsException(dto.registration());

        if (employeeRepository.existsByEmail(dto.email()))
            throw new EmailAlreadyExistsException(dto.email());

        if (employeeRepository.existsByCpf(dto.cpf()))
            throw new CpfAlreadyExistsException(dto.cpf());

        User user = new User();
        user.setRegistration(dto.registration());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        User savedUser = userRepository.save(user);

        Employee employee = mapper.toEntity(dto);
        employee.setUser(savedUser);

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

    // ---------------- helpers ----------------

    private EmployeeResponse toResponse(Employee e) {
        return new EmployeeResponse(
                e.getId(),
                e.getUser().getUserId(),
                e.getUser().getRegistration(),
                e.getEmail(),
                e.getName(),
                e.getCpf(),
                e.getPhone(),
                e.getType()
        );
    }
}