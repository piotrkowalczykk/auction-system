package org.kowal.apigateway.auth;

import org.kowal.apigateway.auth.dto.*;
import org.kowal.security.TokenPair;
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

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}