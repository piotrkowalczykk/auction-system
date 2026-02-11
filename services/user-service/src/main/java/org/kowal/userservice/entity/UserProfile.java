package org.kowal.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private String userId;
    private String email;
    private String nickname;
    private String firstName;
    private String lastName;
    private double sellerRating;
    private double buyerRating;
    private Instant createdAt;
}
