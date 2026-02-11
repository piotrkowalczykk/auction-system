package org.kowal.authservice.service;

import lombok.AllArgsConstructor;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.entity.RefreshToken;
import org.kowal.authservice.exception.custom.InvalidRefreshTokenExceptionAuth;
import org.kowal.authservice.exception.custom.UserNotFoundExceptionAuth;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.authservice.repository.RefreshTokenRepository;
import org.kowal.security.JwtService;
import org.kowal.security.TokenPair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final AuthUserRepository authUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;


    public String createRefreshToken(AuthUser user){
        String refreshTokenValue = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .userId(user.getId())
                .expiration(new Date(System.currentTimeMillis() + 604800000))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshTokenValue;
    }

    @Transactional
    public TokenPair refresh(String refreshTokenValue){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(InvalidRefreshTokenExceptionAuth::new);

        if(refreshToken.isRevoked())
            throw new InvalidRefreshTokenExceptionAuth();

        if(refreshToken.getExpiration().before(new Date()))
            throw new InvalidRefreshTokenExceptionAuth();

        String userId = refreshToken.getUserId();
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(UserNotFoundExceptionAuth::new);

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
