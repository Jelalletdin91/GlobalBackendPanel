package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCompanyAlert(String toEmail, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jelilberjanow83@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Kimlik bitiyor");
        message.setText(text);
        mailSender.send(message);
    }
}