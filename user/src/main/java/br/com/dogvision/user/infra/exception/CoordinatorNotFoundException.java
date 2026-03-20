package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class CoordinatorNotFoundException extends BusinessException {
    public CoordinatorNotFoundException(String registration) {
        super("Coordinator not found with registration: " + registration, HttpStatus.NOT_FOUND);
    }
}
