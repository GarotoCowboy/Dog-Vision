package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class CollaboratorNotFoundException extends BusinessException {
    public CollaboratorNotFoundException(String registration) {
        super("Collaborator not found with registration: " + registration, HttpStatus.NOT_FOUND);

    }
}
