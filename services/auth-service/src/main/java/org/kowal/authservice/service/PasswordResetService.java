package org.kowal.authservice.service;

import lombok.AllArgsConstructor;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.entity.PasswordResetToken;
import org.kowal.authservice.exception.custom.PasswordResetTokenAlreadyUsedException;
import org.kowal.authservice.exception.custom.UserNotFoundException;
import org.kowal.authservice.kafka.producer.PasswordResetProducer;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.authservice.repository.PasswordResetTokenRepository;
import org.kowal.event.grpc.ResetPasswordEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetService {

    private final AuthUserRepository authUserRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetProducer passwordResetProducer;

    public void requestReset(String email) {

        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken(
                token,
                user.getId(),
                new Date(System.currentTimeMillis() + 86400000),
                false
        );

        passwordResetTokenRepository.save(resetToken);
        passwordResetProducer.send(ResetPasswordEvent.newBuilder()
                .setUserId(user.getId())
                .setEmail(email)
                .setResetToken(token)
                .build());
    }

    public void confirmReset(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findById(token)
                .orElseThrow(PasswordResetTokenAlreadyUsedException::new);

        if (resetToken.isUsed())
            throw new PasswordResetTokenAlreadyUsedException();

        if (resetToken.getExpiration().before(new Date()))
            throw new PasswordResetTokenAlreadyUsedException();

        AuthUser user = authUserRepository.findById(resetToken.getUserId())
                .orElseThrow(UserNotFoundException::new);

        user.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
}
