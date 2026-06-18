package br.com.dogvision.dogfeeding.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InsufficientRationStockException extends BusinessException {

    public InsufficientRationStockException(UUID rationId, double available, double requested) {
        super("Ration " + rationId + " has insufficient stock. Available: " + available + ", requested: " + requested, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
