package org.kowal.apigateway.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPublicProfileResponseDto {
    private String id;
    private String nickname;
    private String firstName;
    private String lastName;
    private double sellerRating;
    private double buyerRating;
    private Instant createdAt;
}
