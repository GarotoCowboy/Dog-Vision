package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;

import java.util.UUID;

public record CoordinatorResponse(
        UUID employeeId,
        UUID userId,
        String registration,
        String email,
        String name,
        String cpf,
        String phone,
        EmployeeType type,
        Integer codAdmin
) {}
