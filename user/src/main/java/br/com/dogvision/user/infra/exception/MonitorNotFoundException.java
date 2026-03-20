package br.com.dogvision.user.infra.exception;

import org.springframework.http.HttpStatus;

public class MonitorNotFoundException extends BusinessException {
    public MonitorNotFoundException(String registration) {
        super("Monitor not found with registration: " + registration, HttpStatus.NOT_FOUND);

    }
}
