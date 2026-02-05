package org.kowal.apigateway.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.kowal.auth.grpc.AuthServiceGrpc;
import org.kowal.auth.grpc.RegisterRequest;
import org.kowal.auth.grpc.RegisterResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthGrpcClient {
    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    public AuthGrpcClient(){
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9091)
                .usePlaintext()
                .build();

        blockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public RegisterResponse register(String email, String password){

        RegisterRequest request = RegisterRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();

        RegisterResponse response = blockingStub.register(request);

        return response;
    }
}
