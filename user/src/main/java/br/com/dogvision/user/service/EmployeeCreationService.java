package br.com.dogvision.user.service;

import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.Role;

public interface EmployeeCreationService {

    Employee createEmployee(
            String registration,
            String rawPassword,
            String email,
            String name,
            String cpf,
            String phone,
            EmployeeType type,
            Role role
    );
}
