package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class TrainerNotFoundException extends BusinessException {
    public TrainerNotFoundException(String registration) {
        super("Trainer not found with registration: " + registration, HttpStatus.NOT_FOUND);
    }
}
