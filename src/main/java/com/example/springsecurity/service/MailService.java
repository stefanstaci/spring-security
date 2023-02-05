package com.example.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(String toEmail, String subject, String message) {
        var email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setText(message);
        email.setSubject(subject);

        javaMailSender.send(email);
    }
}
