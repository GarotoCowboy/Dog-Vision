package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class VeterinarianCrmvAlreadyExistsException extends BusinessException {
    public VeterinarianCrmvAlreadyExistsException(String crmv) {
        super("CRMV already exists: " + crmv, HttpStatus.CONFLICT);
    }
}
