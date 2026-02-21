package org.kowal.apigateway.bid.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.kowal.apigateway.bid.dto.PlaceBidRequestDto;
import org.kowal.apigateway.bid.mapper.BidGrpcMapper;
import org.kowal.bidding.grpc.BiddingServiceGrpc;
import org.kowal.bidding.grpc.PlaceBidRequest;
import org.kowal.bidding.grpc.PlaceBidResponse;
import org.springframework.stereotype.Component;

@Component
public class BidGrpcClient {

    @GrpcClient("bidding-service")
    private BiddingServiceGrpc.BiddingServiceBlockingStub biddingServiceBlockingStub;
    private BidGrpcMapper bidGrpcMapper;

    public BidGrpcClient(BidGrpcMapper bidGrpcMapper){
        this.bidGrpcMapper = bidGrpcMapper;
    }

    public PlaceBidResponse placeBid(PlaceBidRequestDto requestDto, String bidderId){
        PlaceBidRequest request = PlaceBidRequest.newBuilder()
                .setAuctionId(requestDto.getAuctionId())
                .setBidderId(bidderId)
                .setAmount(bidGrpcMapper.mapBigDecimalToDecimal(requestDto.getAmount()))
                .build();

        return biddingServiceBlockingStub.placeBid(request);
    }
}
