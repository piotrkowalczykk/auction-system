package org.kowal.apigateway.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.kowal.apigateway.user.dto.UpdateUserProfileRequestDto;
import org.kowal.user.grpc.*;
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

    public GetUserPublicProfileResponse getUserPublicProfile(String userId) {

        GetUserPublicProfileRequest request = GetUserPublicProfileRequest.newBuilder()
                .setUserId(userId)
                .build();

        return  userServiceBlockingStub.getUserPublicProfileById(request);
    }

    public GetUserProfileResponse updateUserProfile(UpdateUserProfileRequestDto request, String userId){
        UpdateUserProfileRequest grpcRequest = UpdateUserProfileRequest.newBuilder()
                .setUserId(userId)
                .setNickname(request.getNickname())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .build();

        return userServiceBlockingStub.updateUserProfile(grpcRequest);
    }
}
