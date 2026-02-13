package org.kowal.userservice.service;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import org.kowal.user.grpc.GetUserProfileResponse;
import org.kowal.user.grpc.GetUserPublicProfileResponse;
import org.kowal.user.grpc.UpdateUserProfileRequest;
import org.kowal.userservice.entity.UserProfile;
import org.kowal.userservice.exception.custom.UserProfileNotFoundException;
import org.kowal.userservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void createUserProfile(String userId, String email) {

        if(userProfileRepository.existsById(userId))
            return;

        UserProfile userProfile = UserProfile.builder()
                .userId(userId)
                .email(email)
                .nickname(generateDefaultNickname(userId))
                .createdAt(Instant.now())
                .build();

        userProfileRepository.save(userProfile);
    }

    public GetUserProfileResponse getUserProfile(String userId){
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(UserProfileNotFoundException::new);

        return GetUserProfileResponse.newBuilder()
                .setUserId(userProfile.getUserId())
                .setEmail(userProfile.getEmail())
                .setNickname(nullSafe(userProfile.getNickname()))
                .setFirstName(nullSafe(userProfile.getFirstName()))
                .setLastName(nullSafe(userProfile.getLastName()))
                .setSellerRating(userProfile.getSellerRating())
                .setBuyerRating(userProfile.getBuyerRating())
                .setCreatedAt(Timestamp.newBuilder().setSeconds(userProfile.getCreatedAt().getEpochSecond()).setNanos(userProfile.getCreatedAt().getNano()))
                .build();
    }

    public GetUserPublicProfileResponse getUserPublicProfile(String userId){
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(UserProfileNotFoundException::new);

        return GetUserPublicProfileResponse.newBuilder()
                .setUserId(userProfile.getUserId())
                .setNickname(nullSafe(userProfile.getNickname()))
                .setFirstName(nullSafe(userProfile.getFirstName()))
                .setLastName(nullSafe(userProfile.getLastName()))
                .setSellerRating(userProfile.getSellerRating())
                .setBuyerRating(userProfile.getBuyerRating())
                .setCreatedAt(Timestamp.newBuilder().setSeconds(userProfile.getCreatedAt().getEpochSecond()).setNanos(userProfile.getCreatedAt().getNano()))
                .build();
    }

    @Transactional
    public GetUserProfileResponse updateUserProfile(UpdateUserProfileRequest request){
        UserProfile userProfile = userProfileRepository.findById(request.getUserId())
                .orElseThrow(UserProfileNotFoundException::new);

        if(!request.getNickname().isEmpty())
            userProfile.setNickname(request.getNickname());

        if(!request.getFirstName().isEmpty())
            userProfile.setFirstName(request.getFirstName());

        if(!request.getLastName().isEmpty())
            userProfile.setLastName(request.getLastName());

        userProfileRepository.save(userProfile);
        return getUserProfile(userProfile.getUserId());
    }

    private String generateDefaultNickname(String userId) {
        return "User_" + userId.substring(0, 8);
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }
}
