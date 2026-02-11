package org.kowal.apigateway.user;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.grpc.UserGrpcClient;
import org.kowal.apigateway.user.dto.UserProfileResponseDto;
import org.kowal.user.grpc.GetUserProfileResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class UserGatewayService {
    private final UserGrpcClient userGrpcClient;

    public UserProfileResponseDto getUserProfile(String userId) {
        GetUserProfileResponse grpcResponse = userGrpcClient.getUserProfile(userId);

        UserProfileResponseDto response = UserProfileResponseDto.builder()
                .id(grpcResponse.getUserId())
                .nickname(grpcResponse.getNickname())
                .email(grpcResponse.getEmail())
                .firstName(grpcResponse.getFirstName())
                .lastName(grpcResponse.getLastName())
                .sellerRating(grpcResponse.getSellerRating())
                .buyerRating(grpcResponse.getBuyerRating())
                .createdAt(Instant.ofEpochSecond(grpcResponse.getCreatedAt().getSeconds(), grpcResponse.getCreatedAt().getNanos()))
                .build();

        return response;
    }

}
