package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.EmployeeCreationService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class EmployeeCreationServiceImpl implements EmployeeCreationService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeCreationServiceImpl(
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Employee createEmployee(
            String registration,
            String rawPassword,
            String email,
            String name,
            String cpf,
            String phone,
            EmployeeType type,
            Role role
    ) {

        // 🔎 Validações de conflito
        if (userRepository.existsByRegistration(registration)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "registration já existe"
            );
        }

        if (employeeRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "email já existe"
            );
        }

        if (employeeRepository.existsByCpf(cpf)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "cpf já existe"
            );
        }

        // 🧾 1) Criar User
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);

        // 👤 2) Criar Employee
        Employee employee = new Employee();
        employee.setUser(savedUser);
        employee.setEmail(email);
        employee.setName(name);
        employee.setCpf(cpf);
        employee.setPhone(phone);
        employee.setType(type);

        return employeeRepository.save(employee);
    }
}
