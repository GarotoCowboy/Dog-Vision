package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email, HttpStatus.CONFLICT);

    }
}
