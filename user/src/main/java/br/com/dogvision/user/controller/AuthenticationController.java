package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.AuthenticationDto;
import br.com.dogvision.user.dto.create.CreateFirstCoordinatorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.dto.response.LoginResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.infra.security.TokenService;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.service.CoordinatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error - Unexpected DogVision failure",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final CoordinatorService coordinatorService;

    @Operation(
            summary = "Realizar login",
            description = "Authenticates a user with registration and password, returning a JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid or missing data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid registration or password",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid AuthenticationDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.registration(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(
            summary = "Cadastrar o primeiro coordenador (Uso único)",
            description = "Endpoint público para inicialização do sistema. Permite cadastrar o primeiro coordenador apenas quando nenhum coordenador ainda existe."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Primeiro coordenador cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CoordinatorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou faltando",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Primeiro coordenador já cadastrado no sistema",
                    content = @Content
            )
    })
    @PostMapping("/first-coordinator")
    public ResponseEntity<CoordinatorResponse> createFirstCoordinator(@RequestBody @Valid CreateFirstCoordinatorRequest data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coordinatorService.createFirstCoordinator(data));
    }
}
