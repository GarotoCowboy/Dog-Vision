package br.com.dogvision.dogfeeding.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
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

    public String getIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var jwt = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
            var claim = jwt.getClaim("collaboratorId");
            if (claim != null && !claim.isNull() && claim.asString() != null) {
                return claim.asString();
            }
            return jwt.getSubject() != null ? jwt.getSubject() : "";
        } catch (JWTVerificationException exception) {
            return "";
        }
    }
}
