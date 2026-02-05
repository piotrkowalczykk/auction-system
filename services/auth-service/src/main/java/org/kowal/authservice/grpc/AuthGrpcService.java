package org.kowal.authservice.grpc;

import io.grpc.stub.StreamObserver;
import org.kowal.auth.grpc.*;

public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver){
        System.out.println("Register email: " + request.getEmail());

        RegisterResponse response = RegisterResponse.newBuilder()
                .setUserId("123")
                .setSuccess(true)
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