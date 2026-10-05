package br.com.dogvision.notification.service;

public interface EmailService {
    void sendTemporaryPasswordEmail(String toEmail, String name, String registration, String temporaryPassword);
}
