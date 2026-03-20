package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resource, UUID id) {
        super(resource + " not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
