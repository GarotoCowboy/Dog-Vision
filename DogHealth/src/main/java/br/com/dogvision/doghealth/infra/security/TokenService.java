package br.com.dogvision.doghealth.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o registration (subject)
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    public List<String> getRolesFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getClaim("roles").asList(String.class);
        } catch (JWTVerificationException exception) {
            return Collections.emptyList();
        }
    }

    public String getIdFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(String.valueOf(token))
                    .getClaim("employeeId").asString();
        }catch (JWTVerificationException exception){
            return "";
        }
    }
}
