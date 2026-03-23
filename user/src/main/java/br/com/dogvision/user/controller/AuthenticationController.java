package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.AuthenticationDto;
import br.com.dogvision.user.dto.response.LoginResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.infra.security.TokenService;
import br.com.dogvision.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor - Falha inesperada no DogVision",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "Endpoints de autenticação de usuários")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Realizar login",
            description = "Autentica o usuário com matrícula e senha, retornando um token JWT"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou ausentes",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Matrícula ou senha incorretos",
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
}