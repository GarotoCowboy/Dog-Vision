package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogMassageRequest;
import br.com.dogvision.doghealth.dto.response.DogMassageResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogMassageRequest;
import br.com.dogvision.doghealth.infra.exception.error.ErrorResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogMassageService;
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
@RequestMapping("/api/v1/doghealth/massage")
@AllArgsConstructor
@Tag(name = "Massages", description = "Endpoints de gerenciamento de massagens de caes")
@SecurityRequirement(name = "bearerAuth")
public class DogMassageController {

    public final TokenService tokenService;
    public final DogMassageService service;


    @Operation(summary = "Cadastrar nova massagem")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "massagem criada com sucesso", content = @Content(schema = @Schema(implementation = DogMassageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<DogMassageResponse> save(@RequestBody @Valid CreateDogMassageRequest dto,
                                                  @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID collaboratorId = UUID.fromString(tokenService.getIdFromToken(token));
        DogMassageResponse response = service.save(dto, collaboratorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar massagens de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogMassageResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<DogMassageResponse>> listMassagesByDogId(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id){
        List<DogMassageResponse> response = service.listByDogId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar massagens mensais de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogMassageResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}/month")
    public ResponseEntity<List<DogMassageResponse>> listMassageByMonth(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Mes da consulta, de 1 a 12", required = true, example = "4")
            @RequestParam int month,
            @Parameter(description = "Ano da consulta", required = true, example = "2026")
            @RequestParam int year
    ){
        List<DogMassageResponse> responses = service.getByMonth(id,month,year);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Listar massagens semanais de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogMassageResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}/week")
    public ResponseEntity<List<DogMassageResponse>> listMassagesByWeek(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Data de referencia da semana", required = true, example = "2026-04-08")
            @RequestParam LocalDate date
    ){
        List<DogMassageResponse> responses = service.getByWeek(id,date);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar ultima massagem de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ultima massagem retornada com sucesso", content = @Content(schema = @Schema(implementation = DogMassageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}/lastWeight")
    public ResponseEntity<Optional<DogMassageResponse>> getLastMassageById(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID id){
        Optional<DogMassageResponse>response = service.getLastMassage(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar uma massagem")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "massagem atualizada com sucesso", content = @Content(schema = @Schema(implementation = DogMassageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "massagem nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<DogMassageResponse> update(
            @Parameter(description = "UUID da massagem", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid UpdateDogMassageRequest dto){
        DogMassageResponse response = service.update(id,dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar massagem por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "massagem deletada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "massagem nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID da massagem", required = true)
            @PathVariable UUID id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
