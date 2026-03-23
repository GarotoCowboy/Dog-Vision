package br.com.dogvision.user.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de um funcionário (todos os campos são opcionais)")
public record UpdateEmployeeRequest(

        @Schema(description = "Novo e-mail do funcionário", example = "novoemail@dogvision.com")
        @Email String email,

        @Schema(description = "Novo nome completo do funcionário", example = "Maria Silva Oliveira")
        String name,

        @Schema(description = "Novo telefone do funcionário (9 a 11 dígitos)", example = "11987654321")
        @Size(min = 9, max = 11) String phone

) {}