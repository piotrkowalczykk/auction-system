package org.kowal.apigateway.user.mapper;

import org.kowal.apigateway.user.dto.UserProfileResponseDto;
import org.kowal.apigateway.user.dto.UserPublicProfileResponseDto;
import org.kowal.user.grpc.GetUserProfileResponse;
import org.kowal.user.grpc.GetUserPublicProfileResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserGrpcMapper {
    public UserProfileResponseDto mapGetUserProfileResponseToUserProfileResponseDto(GetUserProfileResponse response){

        return UserProfileResponseDto.builder()
                .id(response.getUserId())
                .nickname(response.getNickname())
                .email(response.getEmail())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .sellerRating(response.getSellerRating())
                .buyerRating(response.getBuyerRating())
                .createdAt(Instant.ofEpochSecond(response.getCreatedAt().getSeconds(), response.getCreatedAt().getNanos()))
                .build();
    }

    public UserPublicProfileResponseDto mapGetUserPublicProfileResponseToUserPublicProfileResponseDto(GetUserPublicProfileResponse response){

        return UserPublicProfileResponseDto.builder()
                .id(response.getUserId())
                .nickname(response.getNickname())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .sellerRating(response.getSellerRating())
                .buyerRating(response.getBuyerRating())
                .createdAt(Instant.ofEpochSecond(response.getCreatedAt().getSeconds(), response.getCreatedAt().getNanos()))
                .build();
    }


}
