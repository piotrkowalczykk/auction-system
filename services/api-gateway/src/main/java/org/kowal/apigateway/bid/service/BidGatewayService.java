package org.kowal.apigateway.bid.service;

import lombok.AllArgsConstructor;
import org.kowal.apigateway.bid.dto.BidResponseDto;
import org.kowal.apigateway.bid.dto.PlaceBidRequestDto;
import org.kowal.apigateway.bid.grpc.BidGrpcClient;
import org.kowal.bidding.grpc.PlaceBidResponse;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BidGatewayService {

    private final BidGrpcClient bidGrpcClient;

    public BidResponseDto placeBid(PlaceBidRequestDto request, String bidderId) {
        PlaceBidResponse grpcResponse = bidGrpcClient.placeBid(request, bidderId);

        return BidResponseDto.builder()
                .success(grpcResponse.getSuccess())
                .message(grpcResponse.getMessage())
                .build();
    }
}
