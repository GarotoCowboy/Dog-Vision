package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.EmployeeRepository;
import br.com.dogvision.user.service.EmployeeCreationService;
import br.com.dogvision.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @deprecated Utilize {@link UserService} diretamente. Mantido apenas para compatibilidade temporária.
 */
@Deprecated
@Service
@AllArgsConstructor
public class EmployeeCreationServiceImpl implements EmployeeCreationService {

    private final UserService userService;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Employee createEmployee(
            String registration,
            String email,
            String name,
            String phone,
            ShiftEnum shift,
            EmployeeType type,
            Role role
    ) {
        User user = userService.createAccount(registration, email, name, role);

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setEmail(email);
        employee.setName(name);
        employee.setPhone(phone);
        employee.setShift(shift);
        employee.setType(type);

        return employeeRepository.save(employee);
    }
}
