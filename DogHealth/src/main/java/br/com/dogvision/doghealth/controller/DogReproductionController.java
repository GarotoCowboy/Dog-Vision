package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogReproductionRequest;
import br.com.dogvision.doghealth.dto.response.DogReproductionResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogReproductionRequest;
import br.com.dogvision.doghealth.infra.exception.error.ErrorResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogReproductionService;
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
@RequestMapping("/api/v1/doghealth/reproduction")
@AllArgsConstructor
@Tag(name = "Reproduction", description = "Endpoints de gerenciamento do controle reprodutivo (cio) de cadelas")
@SecurityRequirement(name = "bearerAuth")
public class DogReproductionController {

    public final TokenService tokenService;
    public final DogReproductionService service;

    @Operation(summary = "Cadastrar novo registro de cio / controle reprodutivo (Apenas Veterinário)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro reprodutivo criado com sucesso", content = @Content(schema = @Schema(implementation = DogReproductionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão (exclusivo para veterinários)", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<DogReproductionResponse> save(
            @RequestBody @Valid CreateDogReproductionRequest dto,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID veterinarianId = UUID.fromString(tokenService.getIdFromToken(token));
        DogReproductionResponse response = service.save(dto, veterinarianId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar registro de controle reprodutivo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso", content = @Content(schema = @Schema(implementation = DogReproductionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DogReproductionResponse> get(
            @Parameter(description = "UUID do registro reprodutivo", required = true)
            @PathVariable UUID id) {
        DogReproductionResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos os registros de controle reprodutivo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogReproductionResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DogReproductionResponse>> list() {
        List<DogReproductionResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Listar histórico reprodutivo de uma cadela")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogReproductionResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<DogReproductionResponse>> listByDogId(
            @Parameter(description = "UUID da cadela", required = true)
            @PathVariable UUID dogId) {
        List<DogReproductionResponse> responses = service.listByDogId(dogId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar último cio registrado e previsão do próximo de uma cadela")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Último registro retornado com sucesso (ou vazio se inexistente)", content = @Content(schema = @Schema(implementation = DogReproductionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping("/dog/{dogId}/last")
    public ResponseEntity<DogReproductionResponse> getLastByDogId(
            @Parameter(description = "UUID da cadela", required = true)
            @PathVariable UUID dogId) {
        return service.getLastHeatByDogId(dogId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Atualizar registro de controle reprodutivo (Apenas Veterinário)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso", content = @Content(schema = @Schema(implementation = DogReproductionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão (exclusivo para veterinários)", content = @Content)
    })
    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<DogReproductionResponse> update(
            @Parameter(description = "UUID do registro reprodutivo", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid UpdateDogReproductionRequest dto) {
        DogReproductionResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar registro de controle reprodutivo (Apenas Veterinário)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão (exclusivo para veterinários)", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID do registro reprodutivo", required = true)
            @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
