package org.kowal.apigateway.auction.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.kowal.apigateway.auction.dto.CreateAuctionRequestDto;
import org.kowal.apigateway.auction.mapper.AuctionGrpcMapper;
import org.kowal.auction.grpc.*;
import org.springframework.stereotype.Component;


@Component
public class AuctionGrpcClient {
    @GrpcClient("auction-service")
    private AuctionServiceGrpc.AuctionServiceBlockingStub auctionServiceBlockingStub;
    private final AuctionGrpcMapper auctionGrpcMapper;

    public AuctionGrpcClient(AuctionGrpcMapper auctionGrpcMapper) {
        this.auctionGrpcMapper = auctionGrpcMapper;
    }

    public AuctionResponse createAuction(CreateAuctionRequestDto request, String userId){

        CreateAuctionRequest grpcRequest = CreateAuctionRequest.newBuilder()
                .setUserId(userId)
                .setTitle(request.getTitle())
                .setDescription(request.getDescription())
                .setStartPrice(auctionGrpcMapper.mapBigDecimalToDecimal(request.getStartPrice()))
                .setBuyNowPrice(auctionGrpcMapper.mapBigDecimalToDecimal(request.getBuyNowPrice()))
                .setMinIncrement(auctionGrpcMapper.mapBigDecimalToDecimal(request.getMinIncrement()))
                .setEndTime(auctionGrpcMapper.mapInstantToTimestamp(request.getEndTime()))
                .setAuctionType(request.getAuctionType().toString())
                .build();



        return auctionServiceBlockingStub.createAuction(grpcRequest);
    }

    public GetAllAuctionsResponse getAllAuctions(){
        GetAllAuctionsRequest request = GetAllAuctionsRequest.newBuilder().build();
        return auctionServiceBlockingStub.getAllAuctions(request);
    }
}
