package com.mindhub.homebanking.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailService() {

    }

    public void send(String from, String to, Date date, String subject,
                               String text, String attachName,
                               Resource resource) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSentDate(date);
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.addInline(attachName, resource);
        mailSender.send(message);
    }

    //Send OTP
    public void sendOtpMessage(String from, String to, String subject, String message,
                               String attachName, Resource resource) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        helper.addInline(attachName, resource);
        mailSender.send(msg);
    }
}
