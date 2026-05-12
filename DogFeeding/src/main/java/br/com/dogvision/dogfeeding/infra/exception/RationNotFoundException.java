package br.com.dogvision.dogfeeding.infra.exception;

import java.util.UUID;

public class RationNotFoundException extends ResourceNotFoundException {

    public RationNotFoundException(UUID id) {
        super("Ration", id);
    }
}
