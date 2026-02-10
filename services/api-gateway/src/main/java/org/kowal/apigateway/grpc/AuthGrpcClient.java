package org.kowal.apigateway.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.kowal.auth.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class AuthGrpcClient {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    public RegisterResponse register(String email, String password){
        RegisterRequest request = RegisterRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();

        return blockingStub.register(request);
    }

    public LoginResponse login(String email, String password){
        LoginRequest request = LoginRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();

        return blockingStub.login(request);
    }

    public RefreshTokenResponse refresh(String refreshToken){
        RefreshTokenRequest request = RefreshTokenRequest.newBuilder()
                .setRefreshToken(refreshToken)
                .build();

        return blockingStub.refreshToken(request);
    }

    public VerifyEmailResponse verifyEmail(String token){
        VerifyEmailRequest request = VerifyEmailRequest.newBuilder()
                .setToken(token)
                .build();

        return blockingStub.verifyEmail(request);
    }

    public ResendVerificationEmailResponse resendVerification(String email){
        ResendVerificationEmailRequest request = ResendVerificationEmailRequest.newBuilder()
                .setEmail(email)
                .build();

        return blockingStub.resendVerification(request);
    }

    public ResetPasswordResponse resetPassword(String email) {
        ResetPasswordRequest request = ResetPasswordRequest.newBuilder()
                .setEmail(email)
                .build();

        return blockingStub.resetPassword(request);
    }

    public ResetPasswordConfirmResponse resetPasswordConfirm(String token, String newPassword) {
        ResetPasswordConfirmRequest request = ResetPasswordConfirmRequest.newBuilder()
                .setToken(token)
                .setNewPassword(newPassword)
                .build();

        return blockingStub.resetPasswordConfirm(request);
    }
}
