package br.com.dogvision.dogmanagement.controller;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.infra.exceptions.error.ErrorResponse;
import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.service.DogService;

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
@AllArgsConstructor
@RequestMapping("/api/v1/dogs")
@Tag(name = "Dogs", description = "Endpoints de gerenciamento de cães")
@SecurityRequirement(name = "bearerAuth")
public class DogController {

    private final DogService service;


    @Operation(summary = "Buscar cão por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cão encontrado",
                    content = @Content(schema = @Schema(implementation = DogResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Cão não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DogResponse> get(
            @Parameter(description = "UUID do cão", required = true)
            @PathVariable UUID id){

    DogResponse dog =service.getById(id);
    return ResponseEntity.ok(dog);
    }

    @Operation(summary = "Listar todos os cães")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DogResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<DogResponse>> list(){
        List<DogResponse> dogs = service.getAll();
        return ResponseEntity.ok(dogs);
    }


    @Operation(summary = "Cadastrar novo cão")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cão criado com sucesso",
                    content = @Content(schema = @Schema(implementation = DogResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @PostMapping
    public ResponseEntity<DogResponse > create(@RequestBody @Valid CreateDogRequest createDogRequest){
        DogResponse response =service.save(createDogRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Deletar cão por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cão deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cão não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> delete(
            @Parameter(description = "UUID do cão", required = true)
            @PathVariable UUID id){

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar dados de um cão")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cão atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = DogResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cão não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DogResponse> update(
            @Parameter(description = "UUID do cão", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid UpdateDogRequest updateDogRequest) {
        DogResponse updatedDog = service.update(id,updateDogRequest);

        return ResponseEntity.ok(updatedDog);
    }

}
