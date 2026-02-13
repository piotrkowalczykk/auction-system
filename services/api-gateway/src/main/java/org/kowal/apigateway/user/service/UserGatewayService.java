package org.kowal.apigateway.user.service;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.user.grpc.UserGrpcClient;
import org.kowal.apigateway.user.dto.UpdateUserProfileRequestDto;
import org.kowal.apigateway.user.dto.UserProfileResponseDto;
import org.kowal.apigateway.user.dto.UserPublicProfileResponseDto;
import org.kowal.apigateway.user.mapper.UserGrpcMapper;
import org.kowal.user.grpc.GetUserProfileResponse;
import org.kowal.user.grpc.GetUserPublicProfileResponse;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserGatewayService {
    private final UserGrpcClient userGrpcClient;
    private final UserGrpcMapper userGrpcMapper;

    public UserProfileResponseDto getUserProfile(String userId) {
        GetUserProfileResponse grpcResponse = userGrpcClient.getUserProfile(userId);
        return userGrpcMapper.mapGetUserProfileResponseToUserProfileResponseDto(grpcResponse);
    }

    public UserPublicProfileResponseDto getUserPublicProfileById(String userId){
        GetUserPublicProfileResponse grpcResponse = userGrpcClient.getUserPublicProfile(userId);
        return userGrpcMapper.mapGetUserPublicProfileResponseToUserPublicProfileResponseDto(grpcResponse);
    }

    public UserProfileResponseDto updateUserProfile(UpdateUserProfileRequestDto request, String userId){
        GetUserProfileResponse grpcResponse = userGrpcClient.updateUserProfile(request, userId);
        return userGrpcMapper.mapGetUserProfileResponseToUserProfileResponseDto(grpcResponse);
    }

}
