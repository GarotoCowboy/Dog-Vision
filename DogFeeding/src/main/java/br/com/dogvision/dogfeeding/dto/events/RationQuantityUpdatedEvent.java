package br.com.dogvision.dogfeeding.dto.events;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record RationQuantityUpdatedEvent(
        UUID rationId,
        String name,
        double totalRationQuantity,
        double currentRationQuantity
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
