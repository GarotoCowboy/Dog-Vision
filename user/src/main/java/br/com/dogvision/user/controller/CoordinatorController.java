package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.CoordinatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor - Falha inesperada no DogVision",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/employees/coordinators")
@Tag(name = "Coordinators", description = "Endpoints de gerenciamento de coordenadores")
@SecurityRequirement(name = "bearerAuth")
public class CoordinatorController {

    private final CoordinatorService service;

    public CoordinatorController(CoordinatorService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar coordenador por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Coordenador encontrado",
                    content = @Content(schema = @Schema(implementation = CoordinatorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Coordenador não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public CoordinatorResponse getById(
            @Parameter(description = "UUID do coordenador", required = true)
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }

    @Operation(summary = "Buscar coordenador por matrícula")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Coordenador encontrado",
                    content = @Content(schema = @Schema(implementation = CoordinatorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Coordenador não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
    })
    @GetMapping("/registration/{registration}")
    public CoordinatorResponse getByRegistration(
            @Parameter(description = "Matrícula do coordenador", required = true)
            @PathVariable String registration
    ) {
        return service.getByRegistration(registration);
    }

    @Operation(summary = "Listar todos os coordenadores")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CoordinatorResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @GetMapping
    public List<CoordinatorResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Cadastrar novo coordenador")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Coordenador criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CoordinatorResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoordinatorResponse save(@RequestBody @Valid CreateCoordinatorRequest dto) {
        return service.save(dto);
    }

    @Operation(summary = "Deletar coordenador por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Coordenador deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Coordenador não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "UUID do coordenador", required = true)
            @PathVariable UUID id
    ) {
        service.delete(id);
    }
}