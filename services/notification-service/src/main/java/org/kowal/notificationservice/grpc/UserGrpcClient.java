package org.kowal.notificationservice.grpc;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.kowal.user.grpc.GetUserProfileRequest;
import org.kowal.user.grpc.GetUserProfileResponse;
import org.kowal.user.grpc.UserServiceGrpc;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGrpcClient {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    public String getUserEmail(String userId) {

        GetUserProfileRequest request = GetUserProfileRequest.newBuilder()
                .setUserId(userId)
                .build();

        GetUserProfileResponse response = stub.getUserProfile(request);

        return response.getEmail();
    }
}
