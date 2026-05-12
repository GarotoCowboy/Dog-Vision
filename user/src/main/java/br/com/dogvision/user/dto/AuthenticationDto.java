package br.com.dogvision.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User authentication data")
public record AuthenticationDto(

        @Schema(description = "System access registration", example = "COORD001")
        String registration,

        @Schema(description = "Access password", example = "password@123")
        String password

) {}

