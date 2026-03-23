package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateMonitorRequest;
import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.MonitorService;
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
@RequestMapping("/api/v1/employees/monitors")
@Tag(name = "Monitors", description = "Endpoints de gerenciamento de monitores")
@SecurityRequirement(name = "bearerAuth")
public class MonitorController {

    private final MonitorService service;

    public MonitorController(MonitorService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar monitor por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Monitor encontrado",
                    content = @Content(schema = @Schema(implementation = MonitorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Monitor não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public MonitorResponse getById(
            @Parameter(description = "UUID do monitor", required = true)
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }

    @Operation(summary = "Buscar monitor por matrícula")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Monitor encontrado",
                    content = @Content(schema = @Schema(implementation = MonitorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Monitor não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @GetMapping("/registration/{registration}")
    public MonitorResponse getByRegistration(
            @Parameter(description = "Matrícula do monitor", required = true)
            @PathVariable String registration
    ) {
        return service.getByRegistration(registration);
    }

    @Operation(summary = "Listar todos os monitores")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MonitorResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @GetMapping
    public List<MonitorResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Cadastrar novo monitor")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Monitor criado com sucesso",
                    content = @Content(schema = @Schema(implementation = MonitorResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),

    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MonitorResponse save(@RequestBody @Valid CreateMonitorRequest dto) {
        return service.save(dto);
    }

    @Operation(summary = "Deletar monitor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Monitor deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Monitor não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "UUID do monitor", required = true)
            @PathVariable UUID id
    ) {
        service.delete(id);
    }
}