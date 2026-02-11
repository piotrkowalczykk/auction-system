package org.kowal.authservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.exception.custom.EmailAlreadyExistsExceptionAuth;
import org.kowal.authservice.exception.custom.EmailNotVerifiedExceptionAuth;
import org.kowal.authservice.repository.AuthUserRepository;
import org.kowal.security.JwtService;
import org.kowal.security.TokenPair;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    AuthUserRepository authUserRepository;

    @Mock
    RefreshTokenService refreshTokenService;

    @Mock
    EmailVerificationService emailVerificationService;

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void shouldRegisterUserSuccessfully(){

        String email = "test@test.com";
        String password = "password";

        when(authUserRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(password))
                .thenReturn("encoded");

        AuthUser savedUser = AuthUser.builder()
                .id("123")
                .email(email)
                .password("encoded")
                .build();

        when(authUserRepository.save(any(AuthUser.class)))
                .thenReturn(savedUser);

        AuthUser result = authenticationService.register(email, password);
        assertEquals(savedUser, result);
        verify(emailVerificationService)
                .createVerification("123", email);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "test@test.com";

        when(authUserRepository.findByEmail(email))
                .thenReturn(Optional.of(new AuthUser()));

        assertThrows(EmailAlreadyExistsExceptionAuth.class,
                () -> authenticationService.register(email, "password"));
    }

    @Test
    void shouldLoginUserSuccessfully() {
        AuthUser user = AuthUser.builder()
                .id("123")
                .email("test@test.com")
                .emailVerified(true)
                .password("encoded")
                .build();

        when(authUserRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encoded"))
                .thenReturn(true);

        when(jwtService.generateAccessToken("123", user.getEmail()))
                .thenReturn("accessToken");

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn("refreshToken");

        TokenPair tokens = authenticationService.login(user.getEmail(), "password");

        assertEquals("accessToken", tokens.getAccessToken());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotVerified() {

        AuthUser user = AuthUser.builder()
                .id("123")
                .email("test@test.com")
                .emailVerified(false)
                .password("encoded")
                .build();

        when(authUserRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encoded"))
                .thenReturn(true);

        assertThrows(
                EmailNotVerifiedExceptionAuth.class,
                () -> authenticationService.login(user.getEmail(), "password")
        );
    }
}
