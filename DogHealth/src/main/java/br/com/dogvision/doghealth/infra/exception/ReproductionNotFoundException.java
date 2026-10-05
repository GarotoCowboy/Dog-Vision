package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ReproductionNotFoundException extends BusinessException {
    public ReproductionNotFoundException(UUID id) {
        super("Reproductive record not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
