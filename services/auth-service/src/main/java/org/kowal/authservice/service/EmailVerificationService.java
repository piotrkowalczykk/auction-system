package org.kowal.authservice.service;

import lombok.AllArgsConstructor;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.entity.EmailVerificationToken;
import org.kowal.authservice.exception.custom.EmailVerificationTokenAlreadyUsedException;
import org.kowal.authservice.exception.custom.EmailVerificationTokenExpiredException;
import org.kowal.authservice.exception.custom.EmailVerificationTokenNotFoundException;
import org.kowal.authservice.exception.custom.UserNotFoundException;
import org.kowal.authservice.kafka.producer.EmailVerificationProducer;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.authservice.repository.EmailVerificationTokenRepository;
import org.kowal.event.grpc.EmailVerificationEvent;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailVerificationProducer emailVerificationProducer;
    private final AuthUserRepository authUserRepository;

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
                .orElseThrow(UserNotFoundException::new);

        if (user.isEmailVerified())
            return;

        createVerification(user.getId(), user.getEmail());
    }

    public void verify(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findById(token)
                .orElseThrow(EmailVerificationTokenNotFoundException::new);

        if (verificationToken.isUsed())
            throw new EmailVerificationTokenAlreadyUsedException();

        if (verificationToken.getExpiration().before(new Date()))
            throw new EmailVerificationTokenExpiredException();


        AuthUser user = authUserRepository.findById(verificationToken.getUserId())
                        .orElseThrow(UserNotFoundException::new);

        user.setEmailVerified(true);
        verificationToken.setUsed(true);

        authUserRepository.save(user);
        emailVerificationTokenRepository.save(verificationToken);
    }
}
