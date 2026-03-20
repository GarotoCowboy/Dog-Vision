package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class VeterinarianNotFoundException extends BusinessException {
    public VeterinarianNotFoundException(String registration) {
        super("Veterinarian not found with registration: " + registration, HttpStatus.NOT_FOUND);
    }
}
