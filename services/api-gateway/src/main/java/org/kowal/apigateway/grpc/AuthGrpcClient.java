package org.kowal.apigateway.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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
}
