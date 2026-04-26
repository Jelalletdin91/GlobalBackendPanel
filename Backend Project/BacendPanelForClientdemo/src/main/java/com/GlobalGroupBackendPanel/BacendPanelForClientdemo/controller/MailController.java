package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/mail/test")
    public String testMail() {

        emailService.sendCompanyAlert(
                "jelalletdinberjanow797@gmail.com",
                "TEST " + LocalDateTime.now()
        );

        return "Mail sent";
    }
}