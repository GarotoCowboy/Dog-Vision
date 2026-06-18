package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class MassageNotFoundException extends BusinessException {
    public MassageNotFoundException(UUID id) {
        super("Massage not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
