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
            String phone,
            ShiftEnum shift,
            EmployeeType type,
            Role role
    ) {

        // Conflict validations
        if (userRepository.existsByRegistration(registration)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "registration already exists"
            );
        }

        if (employeeRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "email already exists"
            );
        }

        // 1) Create User
        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);

        // 2) Create Employee
        Employee employee = new Employee();
        employee.setUser(savedUser);
        employee.setEmail(email);
        employee.setName(name);
        employee.setPhone(phone);
        employee.setShift(shift);
        employee.setType(type);

        return employeeRepository.save(employee);
    }
}


