package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor - Falha inesperada no DogVision",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
@Tag(name = "Employees", description = "Endpoints de gerenciamento de funcionários")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService service;

    @Operation(summary = "Atualizar dados de um funcionário")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Funcionário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = EmployeeResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
            @Parameter(description = "UUID do funcionário", required = true)
            @PathVariable UUID id,
            @RequestBody UpdateEmployeeRequest dto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, dto));
    }

    @Operation(summary = "Listar todos os funcionários")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> list() {
        return ResponseEntity.ok(service.getAll());
    }
}