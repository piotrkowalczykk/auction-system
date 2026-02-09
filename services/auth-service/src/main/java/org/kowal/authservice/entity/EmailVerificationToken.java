package org.kowal.authservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "email_verification_tokens")
public class EmailVerificationToken {
    @Id
    private String token;
    private String userId;
    private Date expiration;
    private boolean used;
}
