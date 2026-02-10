package org.kowal.apigateway.auth;

import org.kowal.apigateway.auth.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthGatewayService authGatewayService;

    public AuthController(AuthGatewayService authGatewayService) {
        this.authGatewayService = authGatewayService;
    }

    @PostMapping("/register")
    public RegisterResponseDto register(@RequestBody RegisterRequestDto request) {
        return authGatewayService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authGatewayService.login(request);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponseDto refresh(@RequestBody RefreshTokenRequestDto request) {
        return authGatewayService.refresh(request);
    }

    @GetMapping("/verify-email")
    public EmailVerificationResponseDto verifyEmail(@RequestParam String token) {
        return authGatewayService.verifyEmail(token);
    }

    @PostMapping("/reset-password")
    public ResetPasswordResponseDto resetPassword(@RequestBody ResetPasswordRequestDto request){
        return authGatewayService.resetPassword(request);
    }

    @PostMapping("/reset-password/confirm")
    public ResetPasswordConfirmResponseDto resetPasswordConfirm(@RequestBody ResetPasswordConfirmRequestDto request) {
        return null;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}