package org.kowal.apigateway.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.kowal.auth.grpc.AuthServiceGrpc;
import org.kowal.auth.grpc.RegisterRequest;
import org.kowal.auth.grpc.RegisterResponse;
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
}
