package org.kowal.apigateway.bid.controller;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.bid.dto.BidResponseDto;
import org.kowal.apigateway.bid.dto.PlaceBidRequestDto;
import org.kowal.apigateway.bid.service.BidGatewayService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bids")
@AllArgsConstructor
public class BidController {
    private final BidGatewayService bidGatewayService;

    @PostMapping
    public BidResponseDto placeBid(@RequestBody PlaceBidRequestDto request, Authentication authentication){
        String userId = authentication.getName();
        return bidGatewayService.placeBid(request, userId);
    }
}
