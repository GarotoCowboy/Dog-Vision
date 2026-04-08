package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.infra.exception.error.ErrorResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.ConsultationService;
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
@RequestMapping("/api/v1/doghealth/consultation")
@AllArgsConstructor
@Tag(name = "Consultations", description = "Endpoints de gerenciamento de consultas veterinarias")
@SecurityRequirement(name = "bearerAuth")
public class ConsultationController {

    public final TokenService tokenService;
    public final ConsultationService service;

    @Operation(summary = "Cadastrar nova consulta veterinaria")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta criada com sucesso", content = @Content(schema = @Schema(implementation = ConsultationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<ConsultationResponse> save(@RequestBody @Valid CreateConsultationRequest dto,
                                             @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID veterinarianId = UUID.fromString(tokenService.getIdFromToken(token));
        ConsultationResponse response = service.save(dto, veterinarianId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar consulta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada", content = @Content(schema = @Schema(implementation = ConsultationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Consulta nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponse> get(
            @Parameter(description = "UUID da consulta", required = true)
            @PathVariable UUID id){
    ConsultationResponse response = service.getById(id);
    return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todas as consultas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ConsultationResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ConsultationResponse>> list(){
        List<ConsultationResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Atualizar dados de uma consulta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta atualizada com sucesso", content = @Content(schema = @Schema(implementation = ConsultationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Consulta nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<ConsultationResponse> update(
            @Parameter(description = "UUID da consulta", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid UpdateConsultationRequest dto){
        ConsultationResponse consultationResponse = service.update(id,dto);
        return ResponseEntity.ok(consultationResponse);
    }

    @Operation(summary = "Deletar consulta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta deletada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Consulta nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID da consulta", required = true)
            @PathVariable UUID id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
