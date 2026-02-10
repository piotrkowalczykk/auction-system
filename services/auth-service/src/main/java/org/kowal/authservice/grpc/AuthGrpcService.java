package org.kowal.authservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.auth.grpc.*;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.exception.mapper.GrpcExceptionMapper;
import org.kowal.authservice.service.AuthenticationService;
import org.kowal.authservice.service.EmailVerificationService;
import org.kowal.authservice.service.PasswordResetService;
import org.kowal.authservice.service.RefreshTokenService;
import org.kowal.security.TokenPair;

@GrpcService
@AllArgsConstructor
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver){

        try {
            AuthUser user = authenticationService.register(
                    request.getEmail(),
                    request.getPassword()
            );

            RegisterResponse response =
                    RegisterResponse.newBuilder()
                            .setUserId(user.getId())
                            .setSuccess(true)
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            responseObserver.onError(
                    GrpcExceptionMapper
                            .map(ex)
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {

        try{
            TokenPair tokens = authenticationService.login(
                    request.getEmail(),
                    request.getPassword()
            );

            LoginResponse response = LoginResponse.newBuilder()
                    .setAccessToken(tokens.getAccessToken())
                    .setRefreshToken(tokens.getRefreshToken())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            responseObserver.onError(
                    GrpcExceptionMapper
                            .map(ex)
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void refreshToken(RefreshTokenRequest request, StreamObserver<RefreshTokenResponse> responseObserver) {
        try {
            TokenPair newTokens = refreshTokenService.refresh(request.getRefreshToken());

            RefreshTokenResponse response = RefreshTokenResponse.newBuilder()
                    .setAccessToken(newTokens.getAccessToken())
                    .setRefreshToken(newTokens.getRefreshToken())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            responseObserver.onError(
                    GrpcExceptionMapper
                            .map(ex)
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request, StreamObserver<VerifyEmailResponse> responseObserver) {

        emailVerificationService.verify(request.getToken());

        VerifyEmailResponse response = VerifyEmailResponse.newBuilder()
                .setMessage("Email verified successfully")
                .setVerified(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void resendVerification(ResendVerificationEmailRequest request, StreamObserver<ResendVerificationEmailResponse> responseObserver) {
        emailVerificationService.resendVerification(request.getEmail());

        ResendVerificationEmailResponse response = ResendVerificationEmailResponse.newBuilder()
                .setMessage("Verification email resent if the email exists")
                .setResent(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, StreamObserver<ResetPasswordResponse> responseObserver) {
        passwordResetService.requestReset(request.getEmail());

        ResetPasswordResponse response = ResetPasswordResponse.newBuilder()
                .setMessage("Password reset email sent if the email exists")
                .setReset(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void resetPasswordConfirm(ResetPasswordConfirmRequest request, StreamObserver<ResetPasswordConfirmResponse> responseObserver) {
        passwordResetService.confirmReset(request.getToken(), request.getNewPassword());

        ResetPasswordConfirmResponse response = ResetPasswordConfirmResponse.newBuilder()
                .setMessage("Password reset successfully")
                .setReset(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}