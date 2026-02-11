package org.kowal.apigateway.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.kowal.user.grpc.GetUserProfileRequest;
import org.kowal.user.grpc.GetUserProfileResponse;
import org.kowal.user.grpc.UserServiceGrpc;
import org.springframework.stereotype.Component;

@Component
public class UserGrpcClient {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public GetUserProfileResponse getUserProfile(String userId) {

        GetUserProfileRequest request = GetUserProfileRequest.newBuilder()
                .setUserId(userId)
                .build();

        return  userServiceBlockingStub.getUserProfile(request);
    }
}
