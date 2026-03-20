package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class CpfAlreadyExistsException extends BusinessException {
    public CpfAlreadyExistsException(String cpf) {
        super("CPF already exists: " + cpf, HttpStatus.CONFLICT);
    }
}
