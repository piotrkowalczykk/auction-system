package org.kowal.apigateway.auth;

import org.kowal.apigateway.auth.dto.*;
import org.kowal.apigateway.grpc.AuthGrpcClient;
import org.kowal.auth.grpc.*;
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

    public ResendVerificationResponseDto resendVerification(ResendVerificationRequestDto request){
        ResendVerificationEmailResponse grpcResponse = authGrpcClient.resendVerification(request.getEmail());

        return new ResendVerificationResponseDto(
                grpcResponse.getMessage(),
                grpcResponse.getResent()
        );
    }

    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request) {
        ResetPasswordResponse grpcResponse = authGrpcClient.resetPassword(request.getEmail());
        return new ResetPasswordResponseDto(
                grpcResponse.getMessage(),
                grpcResponse.getReset()
        );
    }

    public ResetPasswordConfirmResponseDto resetPasswordConfirm(ResetPasswordConfirmRequestDto request) {
        ResetPasswordConfirmResponse grpcResponse = authGrpcClient.resetPasswordConfirm(request.getToken(), request.getNewPassword());
        return new ResetPasswordConfirmResponseDto(
                grpcResponse.getMessage(),
                grpcResponse.getReset()
        );
    }
}
