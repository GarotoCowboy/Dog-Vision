package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class MedicationNotFoundException extends BusinessException {
    public MedicationNotFoundException(UUID id) {
        super("Medication not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}

