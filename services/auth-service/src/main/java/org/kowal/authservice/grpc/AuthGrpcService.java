package org.kowal.authservice.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.auth.grpc.*;
import org.kowal.authservice.entity.AuthUser;
import org.kowal.authservice.exception.EmailAlreadyExistsException;
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

        } catch (EmailAlreadyExistsException ex) {

            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );

        } catch (Exception ex) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
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