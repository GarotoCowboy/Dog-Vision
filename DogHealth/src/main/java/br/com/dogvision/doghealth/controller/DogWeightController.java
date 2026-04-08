package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogWeightRequest;
import br.com.dogvision.doghealth.infra.exception.error.ErrorResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogWeightService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor - Falha inesperada no DogVision",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/doghealth/weight")
@AllArgsConstructor
@Tag(name = "Weights", description = "Endpoints de gerenciamento de pesagens de caes")
@SecurityRequirement(name = "bearerAuth")
public class DogWeightController {
    public final DogWeightService service;
    public final TokenService tokenService;



    @Operation(summary = "Cadastrar nova pesagem")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pesagem criada com sucesso", content = @Content(schema = @Schema(implementation = DogWeightResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<DogWeightResponse> save(@RequestBody @Valid CreateDogWeightRequest dto,
                                                  @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID monitorId = UUID.fromString(tokenService.getIdFromToken(token));
        DogWeightResponse response = service.save(dto, monitorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar pesagens de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogWeightResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<DogWeightResponse>> listWeightByDogId(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id){
        List<DogWeightResponse> response = service.listByDogId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar pesagens mensais de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogWeightResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}/month")
    public ResponseEntity<List<DogWeightResponse>> listWeightByMonth(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Mes da consulta, de 1 a 12", required = true, example = "4")
            @RequestParam int month,
            @Parameter(description = "Ano da consulta", required = true, example = "2026")
            @RequestParam int year
    ){
        List<DogWeightResponse> responses = service.getByMonth(id,month,year);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Listar pesagens semanais de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogWeightResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}/week")
    public ResponseEntity<List<DogWeightResponse>> listWeightByWeek(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Data de referencia da semana", required = true, example = "2026-04-08")
            @RequestParam LocalDate date
    ){
        List<DogWeightResponse> responses = service.getByWeek(id,date);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar ultima pesagem de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ultima pesagem retornada com sucesso", content = @Content(schema = @Schema(implementation = DogWeightResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}/lastWeight")
    public ResponseEntity<Optional<DogWeightResponse>> getLastWeightById(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id){
        Optional<DogWeightResponse>response = service.getLastWeight(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar uma pesagem")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pesagem atualizada com sucesso", content = @Content(schema = @Schema(implementation = DogWeightResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pesagem nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<DogWeightResponse> update(
            @Parameter(description = "UUID da pesagem", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid UpdateDogWeightRequest dto){
        DogWeightResponse response = service.update(id,dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar pesagem por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pesagem deletada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pesagem nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID da pesagem", required = true)
            @PathVariable UUID id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
