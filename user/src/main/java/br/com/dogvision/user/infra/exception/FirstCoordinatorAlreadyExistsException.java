package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class FirstCoordinatorAlreadyExistsException extends BusinessException {
    public FirstCoordinatorAlreadyExistsException() {
        super("O primeiro coordenador já foi cadastrado no sistema.", HttpStatus.CONFLICT);
    }
}
