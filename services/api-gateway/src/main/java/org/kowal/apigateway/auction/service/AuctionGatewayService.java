package org.kowal.apigateway.auction.service;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.auction.dto.AuctionResponseDto;
import org.kowal.apigateway.auction.dto.CreateAuctionRequestDto;
import org.kowal.apigateway.auction.grpc.AuctionGrpcClient;
import org.kowal.apigateway.auction.mapper.AuctionGrpcMapper;
import org.kowal.auction.grpc.AuctionResponse;
import org.kowal.auction.grpc.GetAllAuctionsResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AuctionGatewayService {
    private final AuctionGrpcClient auctionGrpcClient;
    private final AuctionGrpcMapper auctionGrpcMapper;

    public AuctionResponseDto createAuction(CreateAuctionRequestDto request, String userId){
        AuctionResponse grpcResponse = auctionGrpcClient.createAuction(request, userId);

        return auctionGrpcMapper.mapAuctionResponseToAuctionResponseDto(grpcResponse);
    }

    public List<AuctionResponseDto> getAllAuctions(){
        GetAllAuctionsResponse grpcResponse = auctionGrpcClient.getAllAuctions();
        return grpcResponse.getAuctionsList()
                .stream()
                .map(auctionGrpcMapper::mapAuctionResponseToAuctionResponseDto)
                .toList();
    }
}
