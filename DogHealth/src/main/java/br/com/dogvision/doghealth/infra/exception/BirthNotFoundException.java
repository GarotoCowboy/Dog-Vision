package br.com.dogvision.doghealth.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class BirthNotFoundException extends BusinessException {
    public BirthNotFoundException(UUID id) {
    super("Birth not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
