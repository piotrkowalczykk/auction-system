package org.kowal.authservice.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.auth.grpc.*;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.exception.custom.EmailAlreadyExistsException;
import org.kowal.authservice.exception.mapper.GrpcExceptionMapper;
import org.kowal.authservice.service.AuthService;
import org.kowal.security.TokenPair;

@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;

    public AuthGrpcService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver){

        try {
            AuthUser user = authService.register(
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
            TokenPair tokens = authService.login(
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
            TokenPair newTokens = authService.refresh(request.getRefreshToken());

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

        System.out.println("Verify email token: " + request.getToken());

        VerifyEmailResponse response = VerifyEmailResponse.newBuilder()
                .setVerified(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}