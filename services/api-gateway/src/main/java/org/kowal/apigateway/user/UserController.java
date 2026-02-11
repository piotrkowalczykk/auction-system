package org.kowal.apigateway.user;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.user.dto.UserProfileResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserGatewayService userGatewayService;

    @GetMapping("/me")
    public UserProfileResponseDto getCurrentUserProfile(Authentication authentication){
        String userId = authentication.getName();
        return userGatewayService.getUserProfile(userId);
    }
}
