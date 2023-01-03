package com.mindhub.homebanking.models;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@antartidabank.com");
        message.setTo(to);
        message.setSubject("Transfer notification");
        message.setText("A transfer for $ has been made from your account VIN");
        mailSender.send(message);
    }
}
