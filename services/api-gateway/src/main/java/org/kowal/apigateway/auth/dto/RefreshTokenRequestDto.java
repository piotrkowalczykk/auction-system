package org.kowal.apigateway.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenRequestDto {
    @NotBlank(message = "Refresh token is required")
    @Size(min = 20, max = 500, message = "Invalid refresh token")
    private String refreshToken;
}
