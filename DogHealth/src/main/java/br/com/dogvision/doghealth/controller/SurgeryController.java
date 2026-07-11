package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogSurgeryRequest;
import br.com.dogvision.doghealth.dto.response.DogSurgeryResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogSurgeryRequest;
import br.com.dogvision.doghealth.infra.exception.error.ErrorResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogSurgeryService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor - Falha inesperada no DogVision",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/doghealth/surgery")
@AllArgsConstructor
@Tag(name = "Surgeries", description = "Endpoints de gerenciamento de cirurgias veterinarias")
@SecurityRequirement(name = "bearerAuth")
public class SurgeryController {
    public final TokenService tokenService;
    public final DogSurgeryService service;


    @Operation(summary = "Cadastrar nova cirurgia veterinaria")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cirurgia criada com sucesso", content = @Content(schema = @Schema(implementation = DogSurgeryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<DogSurgeryResponse> save(@RequestBody @Valid CreateDogSurgeryRequest dto,
                                                   @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID veterinarianId = UUID.fromString(tokenService.getIdFromToken(token));
        DogSurgeryResponse response = service.save(dto, veterinarianId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar cirurgia por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cirurgia encontrada", content = @Content(schema = @Schema(implementation = DogSurgeryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cirurgia nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DogSurgeryResponse> get(
            @Parameter(description = "UUID da cirurgia", required = true)
            @PathVariable UUID id) {
        DogSurgeryResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar cirurgias de um cao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogSurgeryResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<DogSurgeryResponse>> listByDogId(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID dogId) {
        List<DogSurgeryResponse> responses = service.findAllByDogId(dogId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Listar cirurgias por periodo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagina retornada com sucesso", content = @Content(schema = @Schema(implementation = DogSurgeryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<DogSurgeryResponse>> listByPeriod(
            @Parameter(description = "Inicio do periodo", required = true, example = "2026-05-01T00:00:00")
            @RequestParam LocalDateTime startsAt,
            @Parameter(description = "Fim do periodo", required = true, example = "2026-05-31T23:59:59")
            @RequestParam LocalDateTime endsAt,
            @Parameter(description = "Quantidade de paginas", example = "0")
            @RequestParam(defaultValue = "0") int pages,
            @Parameter(description = "Quantidade de itens por pagina", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<DogSurgeryResponse> responses = service.findByDateTimeOfSurgeryBetween(startsAt, endsAt, pages, size);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Listar cirurgias de um cao por periodo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagina retornada com sucesso", content = @Content(schema = @Schema(implementation = DogSurgeryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/dog/{dogId}/period")
    public ResponseEntity<Page<DogSurgeryResponse>> listByDogIdAndPeriod(
            @Parameter(description = "UUID do cao", required = true)
            @PathVariable UUID dogId,
            @Parameter(description = "Inicio do periodo", required = true, example = "2026-05-01T00:00:00")
            @RequestParam LocalDateTime startsAt,
            @Parameter(description = "Fim do periodo", required = true, example = "2026-05-31T23:59:59")
            @RequestParam LocalDateTime endsAt,
            @Parameter(description = "Quantidade de paginas", example = "0")
            @RequestParam(defaultValue = "0") int pages,
            @Parameter(description = "Quantidade de itens por pagina", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<DogSurgeryResponse> responses = service.findByDogIdAndDateTimeOfSurgeryBetween(dogId, startsAt, endsAt, pages, size);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Atualizar dados de uma cirurgia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cirurgia atualizada com sucesso", content = @Content(schema = @Schema(implementation = DogSurgeryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cirurgia nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<DogSurgeryResponse> update(
            @Parameter(description = "UUID da cirurgia", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid UpdateDogSurgeryRequest dto) {
        DogSurgeryResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar cirurgia por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cirurgia deletada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cirurgia nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID da cirurgia", required = true)
            @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
