package org.kowal.apigateway.auth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.kowal.apigateway.auth.service.AuthGatewayService;
import org.kowal.apigateway.auth.dto.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthGatewayService authGatewayService;

    @PostMapping("/register")
    public RegisterResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        return authGatewayService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authGatewayService.login(request);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponseDto refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        return authGatewayService.refresh(request);
    }

    @GetMapping("/verify-email")
    public EmailVerificationResponseDto verifyEmail(@Valid @RequestParam String token) {
        return authGatewayService.verifyEmail(token);
    }

    @PostMapping("/resend-verification")
    public ResendVerificationResponseDto resendVerification(@Valid @RequestBody ResendVerificationRequestDto request) {
        return authGatewayService.resendVerification(request);
    }

    @PostMapping("/reset-password")
    public ResetPasswordResponseDto resetPassword(@Valid @RequestBody ResetPasswordRequestDto request){
        return authGatewayService.resetPassword(request);
    }

    @PostMapping("/reset-password/confirm")
    public ResetPasswordConfirmResponseDto resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmRequestDto request) {
        return authGatewayService.resetPasswordConfirm(request);
    }
}