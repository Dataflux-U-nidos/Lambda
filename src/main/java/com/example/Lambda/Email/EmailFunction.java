package com.example.Lambda.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EmailFunction implements Function<EmailRequest, String> {

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired 
    private EmailTemplateFactory emailTemplateFactory;

    @Override
    public String apply(EmailRequest request) {

        String html = emailTemplateFactory.getTemplate(request);        
        try {
            emailSenderService.sendEmail(request.getTo(), request.getSubject(), html);
            return "Email sent successfully to: " + request.getTo();
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    // @Bean is optional, but we can define it explicitly if needed
    @Bean
    public Function<EmailRequest, String> sendEmail() {
        return this::apply;
    }
}