package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogBirthRequest;
import br.com.dogvision.doghealth.infra.exception.error.ErrorResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogsBirthService;
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
@RequestMapping("/api/v1/doghealth/birth")
@AllArgsConstructor
@Tag(name = "Births", description = "Endpoints de gerenciamento de partos de caes")
@SecurityRequirement(name = "bearerAuth")
public class DogBirthController {

    public final TokenService tokenService;
    public final DogsBirthService service;

    @Operation(summary = "Cadastrar novo parto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Parto criado com sucesso", content = @Content(schema = @Schema(implementation = DogBirthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<DogBirthResponse> save(@RequestBody @Valid CreateDogBirthRequest dto,
                                                 @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID veterinarianId = UUID.fromString(tokenService.getIdFromToken(token));
        DogBirthResponse response = service.save(dto, veterinarianId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar parto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parto encontrado", content = @Content(schema = @Schema(implementation = DogBirthResponse.class))),
            @ApiResponse(responseCode = "404", description = "Parto nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DogBirthResponse> get(
            @Parameter(description = "UUID do parto", required = true)
            @PathVariable UUID id){
        DogBirthResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos os partos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogBirthResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DogBirthResponse>> list(){
        List<DogBirthResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Atualizar dados de um parto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parto atualizado com sucesso", content = @Content(schema = @Schema(implementation = DogBirthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Parto nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<DogBirthResponse> update(
            @Parameter(description = "UUID do parto", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid UpdateDogBirthRequest dto){
        DogBirthResponse response = service.update(id,dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar parto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Parto deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Parto nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissao", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID do parto", required = true)
            @PathVariable UUID id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
