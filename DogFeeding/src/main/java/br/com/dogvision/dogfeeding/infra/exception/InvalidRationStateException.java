package br.com.dogvision.dogfeeding.infra.exception;

import org.springframework.http.HttpStatus;

public class InvalidRationStateException extends BusinessException {

    public InvalidRationStateException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
