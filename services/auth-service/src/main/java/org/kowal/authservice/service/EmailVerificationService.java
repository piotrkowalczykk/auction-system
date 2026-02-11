package org.kowal.authservice.service;

import lombok.AllArgsConstructor;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.entity.EmailVerificationToken;
import org.kowal.authservice.exception.custom.EmailVerificationTokenAlreadyUsedExceptionAuth;
import org.kowal.authservice.exception.custom.EmailVerificationTokenExpiredExceptionAuth;
import org.kowal.authservice.exception.custom.EmailVerificationTokenNotFoundExceptionAuth;
import org.kowal.authservice.exception.custom.UserNotFoundExceptionAuth;
import org.kowal.authservice.kafka.producer.EmailVerificationProducer;
import org.kowal.authservice.kafka.producer.UserCreatedProducer;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.authservice.repository.EmailVerificationTokenRepository;
import org.kowal.event.grpc.EmailVerificationEvent;
import org.kowal.event.grpc.UserCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailVerificationProducer emailVerificationProducer;
    private final AuthUserRepository authUserRepository;
    private final UserCreatedProducer userCreatedProducer;

    public void createVerification(String userId, String email) {
        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken(
                token,
                userId,
                new Date(System.currentTimeMillis() + 86400000),
                false
        );

        emailVerificationTokenRepository.save(verificationToken);

        emailVerificationProducer.send(
                EmailVerificationEvent.newBuilder()
                        .setUserId(userId)
                        .setEmail(email)
                        .setVerificationToken(token)
                        .build());
    }

    public void resendVerification(String email) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(UserNotFoundExceptionAuth::new);

        if (user.isEmailVerified())
            return;

        createVerification(user.getId(), user.getEmail());
    }

    public void verify(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findById(token)
                .orElseThrow(EmailVerificationTokenNotFoundExceptionAuth::new);

        if (verificationToken.isUsed())
            throw new EmailVerificationTokenAlreadyUsedExceptionAuth();

        if (verificationToken.getExpiration().before(new Date()))
            throw new EmailVerificationTokenExpiredExceptionAuth();


        AuthUser user = authUserRepository.findById(verificationToken.getUserId())
                        .orElseThrow(UserNotFoundExceptionAuth::new);

        user.setEmailVerified(true);
        verificationToken.setUsed(true);

        userCreatedProducer.send(UserCreatedEvent.newBuilder()
                .setUserId(user.getId())
                .setEmail(user.getEmail())
                .build());

        authUserRepository.save(user);
        emailVerificationTokenRepository.save(verificationToken);
    }
}
