package br.com.dogvision.notification.service.imp;

import br.com.dogvision.notification.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from:${spring.mail.username:nao-responda@dogvision.com}}")
    private String fromEmail;

    @Override
    public void sendTemporaryPasswordEmail(String toEmail, String name, String registration, String temporaryPassword) {
        log.info("Preparing to send temporary password email to [{}] (Registration: [{}])", toEmail, registration);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String sender = (fromEmail != null && !fromEmail.isBlank()) ? fromEmail : "nao-responda@dogvision.com";
            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject("Bem-vindo ao Dog-Vision - Seus dados de acesso");

            String htmlContent = buildWelcomeEmailHtml(name, registration, toEmail, temporaryPassword);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Welcome email sent successfully to [{}]", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to compose or send email to [{}]: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar e-mail de boas-vindas: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while sending email to [{}]: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Erro inesperado no envio de e-mail: " + e.getMessage(), e);
        }
    }

    private String buildWelcomeEmailHtml(String name, String registration, String email, String temporaryPassword) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f6f8;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background-color: #ffffff;
                        border-radius: 8px;
                        padding: 30px;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        text-align: center;
                        border-bottom: 2px solid #2b6cb0;
                        padding-bottom: 15px;
                        margin-bottom: 20px;
                    }
                    .header h1 {
                        color: #2b6cb0;
                        margin: 0;
                        font-size: 24px;
                    }
                    .credentials {
                        background-color: #ebf8ff;
                        border-left: 4px solid #3182ce;
                        padding: 15px 20px;
                        margin: 20px 0;
                        border-radius: 4px;
                    }
                    .credential-item {
                        margin-bottom: 8px;
                        font-size: 15px;
                    }
                    .credential-item strong {
                        color: #2d3748;
                    }
                    .password-box {
                        font-family: 'Courier New', monospace;
                        font-size: 18px;
                        font-weight: bold;
                        color: #c53030;
                        background: #fff;
                        padding: 6px 12px;
                        border-radius: 4px;
                        display: inline-block;
                        border: 1px dashed #feb2b2;
                    }
                    .warning {
                        background-color: #fffaf0;
                        border-left: 4px solid #dd6b20;
                        padding: 12px 16px;
                        margin-top: 20px;
                        font-size: 14px;
                        color: #7b341e;
                        border-radius: 4px;
                    }
                    .footer {
                        text-align: center;
                        font-size: 12px;
                        color: #a0aec0;
                        margin-top: 30px;
                        border-top: 1px solid #e2e8f0;
                        padding-top: 15px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Dog-Vision</h1>
                    </div>
                    <p>Olá, <strong>%s</strong>!</p>
                    <p>Você foi cadastrado no sistema <strong>Dog-Vision</strong> pela coordenação. Abaixo estão suas credenciais para acesso à plataforma:</p>
                    
                    <div class="credentials">
                        <div class="credential-item"><strong>Matrícula (Login):</strong> %s</div>
                        <div class="credential-item"><strong>E-mail:</strong> %s</div>
                        <div class="credential-item">
                            <strong>Senha Temporária:</strong><br/>
                            <span class="password-box">%s</span>
                        </div>
                    </div>

                    <div class="warning">
                        ⚠️ <strong>Importante:</strong> Por razões de segurança, utilize esta senha provisória para realizar seu primeiro acesso e faça a alteração da sua senha imediatamente no sistema.
                    </div>

                    <div class="footer">
                        Este é um e-mail automático gerado pelo sistema Dog-Vision. Por favor, não responda a este e-mail.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(name, registration, email, temporaryPassword);
    }
}
