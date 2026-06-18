package br.com.dogvision.dogfeeding.infra.exception;

import java.util.UUID;

public class FeedingPlanNotFoundException extends ResourceNotFoundException {

    public FeedingPlanNotFoundException(UUID id) {
        super("Feeding plan", id);
    }
}
