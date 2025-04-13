package com.example.Lambda.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("sendEmail")
public class EmailFunction implements Function<EmailRequest, String> {

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public String apply(EmailRequest request) {
        try {
            emailSenderService.sendEmail(request.getTo(), request.getSubject(), request.getHtml());
            return "Email sent successfully to: " + request.getTo();
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}