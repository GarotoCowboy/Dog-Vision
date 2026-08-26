package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateMedicationRequest;
import br.com.dogvision.doghealth.dto.response.MedicationResponse;
import br.com.dogvision.doghealth.infra.exception.error.ErrorResponse;
import br.com.dogvision.doghealth.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/doghealth/medication")
@AllArgsConstructor
@Tag(name = "Medications", description = "Endpoints de gerenciamento de medicamentos")
@SecurityRequirement(name = "bearerAuth")
public class MedicationController {

    private final MedicationService service;

    @Operation(summary = "Cadastrar novo medicamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medicamento cadastrado com sucesso", content = @Content(schema = @Schema(implementation = MedicationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<MedicationResponse> save(@RequestBody @Valid CreateMedicationRequest dto) {
        MedicationResponse response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar medicamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicamento encontrado", content = @Content(schema = @Schema(implementation = MedicationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Medicamento nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponse> get(
            @Parameter(description = "UUID do medicamento", required = true)
            @PathVariable UUID id) {
        MedicationResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos os medicamentos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MedicationResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MedicationResponse>> list() {
        List<MedicationResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Deletar medicamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medicamento deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Medicamento nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID do medicamento", required = true)
            @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

