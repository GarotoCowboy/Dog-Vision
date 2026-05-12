package br.com.dogvision.dogfeeding.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class RationInUseException extends BusinessException {

    public RationInUseException(UUID rationId) {
        super("Ration " + rationId + " is already linked to feeding history and cannot be deleted", HttpStatus.CONFLICT);
    }
}
