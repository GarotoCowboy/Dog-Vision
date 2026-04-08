package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class WeightNotFoundException extends BusinessException {
    public WeightNotFoundException(UUID id) {
        super("Weight not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
