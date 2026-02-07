package org.kowal.authservice.service;

import lombok.AllArgsConstructor;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.entity.RefreshToken;
import org.kowal.authservice.exception.EmailAlreadyExistsException;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.authservice.repository.RefreshTokenRepository;
import org.kowal.security.JwtService;
import org.kowal.security.TokenPair;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;


    public AuthUser register(String email, String password) {
        authUserRepository.findByEmail(email).ifPresent(user -> {
            throw new EmailAlreadyExistsException(email);
        });

        AuthUser user = AuthUser.builder()
                .email(email)
                .password(password)
                .emailVerified(false)
                .build();

        return authUserRepository.save(user);
    }

    public TokenPair login(String email, String password) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow();

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .userId(user.getId())
                .expiration(new Date(System.currentTimeMillis() + 604800000))
                .revoked(false)
                .build();

        refreshTokenRepository.save(token);

        return new TokenPair(accessToken, refreshToken);
    }
}
