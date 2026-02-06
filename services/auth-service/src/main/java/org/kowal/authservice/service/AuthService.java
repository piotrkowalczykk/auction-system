package org.kowal.authservice.service;

import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.exception.EmailAlreadyExistsException;
import org.kowal.authservice.repository.AuthUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthUserRepository authUserRepository;

    public AuthService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

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
}
