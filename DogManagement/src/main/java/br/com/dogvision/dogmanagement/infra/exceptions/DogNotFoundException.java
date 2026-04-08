package br.com.dogvision.dogmanagement.infra.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DogNotFoundException extends BusinessException {
    public DogNotFoundException(UUID id) {
        super("Dog not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
