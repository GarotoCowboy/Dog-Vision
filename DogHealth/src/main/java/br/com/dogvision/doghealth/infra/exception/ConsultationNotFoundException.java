package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ConsultationNotFoundException extends BusinessException {
    public ConsultationNotFoundException(UUID id) {
        super("Consultation not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
