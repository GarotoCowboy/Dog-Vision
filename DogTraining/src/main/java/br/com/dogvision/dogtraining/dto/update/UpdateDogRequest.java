package br.com.dogvision.dogtraining.dto.update;

public record UpdateDogRequest(
        Integer monthYear,
        Integer day,
        String evaluationType
) {
}
