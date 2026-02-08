package org.kowal.authservice.service;

import lombok.AllArgsConstructor;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.entity.RefreshToken;
import org.kowal.authservice.exception.custom.EmailAlreadyExistsException;
import org.kowal.authservice.exception.custom.InvalidCredentialsException;
import org.kowal.authservice.exception.custom.InvalidRefreshTokenException;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.authservice.repository.RefreshTokenRepository;
import org.kowal.security.JwtService;
import org.kowal.security.TokenPair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthUser register(String email, String password) {
        authUserRepository.findByEmail(email).ifPresent(user -> {
            throw new EmailAlreadyExistsException(email);
        });

        AuthUser user = AuthUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .emailVerified(false)
                .build();

        return authUserRepository.save(user);
    }

    public TokenPair login(String email, String password) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidCredentialsException();

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

    public TokenPair refresh(String refreshTokenValue){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(InvalidRefreshTokenException::new);

        if(refreshToken.isRevoked())
            throw new InvalidRefreshTokenException();

        if(refreshToken.getExpiration().before(new Date()))
            throw new InvalidRefreshTokenException();

        String userId = refreshToken.getUserId();
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow();

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken newToken = RefreshToken.builder()
                .token(newRefreshToken)
                .userId(user.getId())
                .expiration(new Date(System.currentTimeMillis() + 604800000))
                .revoked(false)
                .build();

        refreshTokenRepository.save(newToken);

        return new TokenPair(newAccessToken, newRefreshToken);
    }
}
