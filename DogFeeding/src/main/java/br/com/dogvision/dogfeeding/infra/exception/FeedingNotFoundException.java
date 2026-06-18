package br.com.dogvision.dogfeeding.infra.exception;

import java.util.UUID;

public class FeedingNotFoundException extends ResourceNotFoundException {

    public FeedingNotFoundException(UUID id) {
        super("Feeding", id);
    }
}
