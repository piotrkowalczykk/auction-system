package org.kowal.notificationservice.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String token){
        String link = "http://localhost:8080/auth/verify-email?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText("Please click the following link to verify your email: " + link);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String email, String token){
        // String link = "http://localhost:8080/auth/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        //message.setText("Please click the following link to reset your password: " + link);
        message.setText("Copy the following token to reset your password: " + token);
        mailSender.send(message);
    }
}
