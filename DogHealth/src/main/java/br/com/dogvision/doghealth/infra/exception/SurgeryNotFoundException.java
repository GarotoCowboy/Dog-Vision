package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class SurgeryNotFoundException extends BusinessException {
    public SurgeryNotFoundException(UUID id) {
        super("Surgery not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
