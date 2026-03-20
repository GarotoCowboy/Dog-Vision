package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;

import java.util.UUID;

public record MonitorResponse(
        UUID employeeId,
        UUID userId,
        String registration,
        String email,
        String name,
        String cpf,
        String phone,
        EmployeeType type,
        String shift
) {}
