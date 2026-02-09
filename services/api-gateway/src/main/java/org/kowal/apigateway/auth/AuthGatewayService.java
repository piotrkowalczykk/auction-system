package org.kowal.apigateway.auth;

import org.kowal.apigateway.auth.dto.*;
import org.kowal.apigateway.grpc.AuthGrpcClient;
import org.kowal.auth.grpc.LoginResponse;
import org.kowal.auth.grpc.RefreshTokenResponse;
import org.kowal.auth.grpc.RegisterResponse;
import org.kowal.auth.grpc.VerifyEmailResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthGatewayService {
    private final AuthGrpcClient authGrpcClient;

    public AuthGatewayService(AuthGrpcClient authGrpcClient) {
        this.authGrpcClient = authGrpcClient;
     }

    public RegisterResponseDto register(RegisterRequestDto request) {

        RegisterResponse grpcResponse =
                authGrpcClient.register(request.getEmail(), request.getPassword());

        return new RegisterResponseDto(
                grpcResponse.getUserId(),
                grpcResponse.getSuccess()
        );
    }

    public LoginResponseDto login(LoginRequestDto request){
        LoginResponse grpcResponse = authGrpcClient.login(request.getEmail(), request.getPassword());

        return new LoginResponseDto(
                grpcResponse.getAccessToken(),
                grpcResponse.getRefreshToken()
        );
    }

    public RefreshTokenResponseDto refresh(RefreshTokenRequestDto request){
        RefreshTokenResponse grpcResponse = authGrpcClient.refresh(request.getRefreshToken());

        return new RefreshTokenResponseDto(
                grpcResponse.getAccessToken(),
                grpcResponse.getRefreshToken()
        );
    }

    public EmailVerificationResponseDto verifyEmail(String token) {
        VerifyEmailResponse grpcResponse = authGrpcClient.verifyEmail(token);
        return new EmailVerificationResponseDto(
                grpcResponse.getMessage(),
                grpcResponse.getVerified()
        );
    }
}
