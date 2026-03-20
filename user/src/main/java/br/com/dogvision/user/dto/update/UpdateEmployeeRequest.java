package br.com.dogvision.user.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateEmployeeRequest(
        @Email String email,
        String name,
        @Size(min = 9, max = 11) String phone
) {}
