package org.kowal.apigateway.auth;

import org.kowal.apigateway.auth.dto.LoginRequestDto;
import org.kowal.apigateway.auth.dto.LoginResponseDto;
import org.kowal.apigateway.auth.dto.RegisterRequestDto;
import org.kowal.apigateway.auth.dto.RegisterResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}