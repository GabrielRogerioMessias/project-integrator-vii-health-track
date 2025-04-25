package org.projetointegrador.unifio.projectintegratorviibackend.services;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String email, String token, String subject, String path, String message) {
        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toUriString();
            String content = """
                    <div style="font-family: Arial, sans-serif; max-width: 680px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9;">
                        <h1 style="color: #333;">%s</h1>
                        <p style="font-size: 16px; color: #555;">%s</p>
                        <a href="%s" style="display: inline-block; margin: 20px 0; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 4px;">
                            Confirmar e-mail
                        </a>
                        <p style="font-size: 14px; color: #777;">Se o botão não funcionar, copie e cole este link no seu navegador:</p>
                        <p style="font-size: 14px; color: #007bff;">%s</p>
                        <p style="font-size: 12px; color: #aaa;">Esta é uma mensagem automática. Por favor, não responda.</p>
                    </div>
                    """.formatted(subject, message, actionUrl, actionUrl);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String email, String verificationToken) {
        String subject = "Verificação de e-mail";
        String path = "/api/auth/verify";
        String message = "Clique no botão abaixo para verificar seu endereço de e-mail";
        sendEmail(email, verificationToken, subject, path, message);
    }

    public void sendForgotPasswordEmail(String email, String resetToken) {
        String subject = "Validar - Esqueceu sua senha";
        String path = "/api/auth/verify";
        String message = "Clique em RESETAR para resetar sua senha";
        sendEmail(email, resetToken, subject, path, message);
    }

}
