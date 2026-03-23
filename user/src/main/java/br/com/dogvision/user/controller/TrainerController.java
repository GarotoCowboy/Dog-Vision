package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.TrainerService;
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
@RequestMapping("/api/v1/employees/trainers")
@Tag(name = "Trainers", description = "Endpoints de gerenciamento de treinadores")
@SecurityRequirement(name = "bearerAuth")
public class TrainerController {

    private final TrainerService service;

    public TrainerController(TrainerService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar treinador por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Treinador encontrado",
                    content = @Content(schema = @Schema(implementation = TrainerResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Treinador não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public TrainerResponse getById(
            @Parameter(description = "UUID do treinador", required = true)
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }

    @Operation(summary = "Buscar treinador por matrícula")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Treinador encontrado",
                    content = @Content(schema = @Schema(implementation = TrainerResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Treinador não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping("/registration/{registration}")
    public TrainerResponse getByRegistration(
            @Parameter(description = "Matrícula do treinador", required = true)
            @PathVariable String registration
    ) {
        return service.getByRegistration(registration);
    }

    @Operation(summary = "Listar todos os treinadores")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainerResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping
    public List<TrainerResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Cadastrar novo treinador")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Treinador criado com sucesso",
                    content = @Content(schema = @Schema(implementation = TrainerResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerResponse save(@RequestBody @Valid CreateTrainerRequest dto) {
        return service.save(dto);
    }

    @Operation(summary = "Deletar treinador por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Treinador deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Treinador não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "UUID do treinador", required = true)
            @PathVariable UUID id
    ) {
        service.delete(id);
    }
}