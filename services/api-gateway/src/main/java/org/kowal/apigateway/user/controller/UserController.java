package org.kowal.apigateway.user.controller;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.user.service.UserGatewayService;
import org.kowal.apigateway.user.dto.UpdateUserProfileRequestDto;
import org.kowal.apigateway.user.dto.UserProfileResponseDto;
import org.kowal.apigateway.user.dto.UserPublicProfileResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}")
    public UserPublicProfileResponseDto getUserPublicProfileById(@PathVariable String userId){
        return userGatewayService.getUserPublicProfileById(userId);
    }

    @PutMapping("/me")
    public UserProfileResponseDto updateUserProfile(Authentication authentication, @RequestBody UpdateUserProfileRequestDto request){
        String userId = authentication.getName();
        return userGatewayService.updateUserProfile(request, userId);
    }
}
