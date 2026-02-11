package org.kowal.authservice.service;

import lombok.AllArgsConstructor;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.exception.custom.*;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.security.JwtService;
import org.kowal.security.TokenPair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthUserRepository authUserRepository;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthUser register(String email, String password) {
        authUserRepository.findByEmail(email).ifPresent(user -> {
            throw new EmailAlreadyExistsExceptionAuth(email);
        });

        AuthUser user = AuthUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .emailVerified(false)
                .build();

        user = authUserRepository.save(user);
        emailVerificationService.createVerification(user.getId(), email);
        return user;
    }

    public TokenPair login(String email, String password) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsExceptionAuth::new);

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidCredentialsExceptionAuth();

        if(!user.isEmailVerified())
            throw new EmailNotVerifiedExceptionAuth(email);

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenPair(accessToken, refreshToken);
    }
}
