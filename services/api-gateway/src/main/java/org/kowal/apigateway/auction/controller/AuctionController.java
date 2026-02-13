package org.kowal.apigateway.auction.controller;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.auction.service.AuctionGatewayService;
import org.kowal.apigateway.auction.dto.CreateAuctionRequestDto;
import org.kowal.apigateway.auction.dto.AuctionResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions")
@AllArgsConstructor
public class AuctionController {
    private final AuctionGatewayService auctionGatewayService;

    @PostMapping
    public AuctionResponseDto createAuction(@RequestBody CreateAuctionRequestDto request, Authentication authentication){
        String userId = authentication.getName();
        return auctionGatewayService.createAuction(request, userId);
    }
}
