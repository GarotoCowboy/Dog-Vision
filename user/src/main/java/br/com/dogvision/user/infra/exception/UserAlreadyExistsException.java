package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException(String registration) {
        super("User already exists with registration: " + registration, HttpStatus.CONFLICT);
    }
}
