package org.kowal.auctionservice.service;

import lombok.AllArgsConstructor;
import org.kowal.auction.grpc.AuctionResponse;
import org.kowal.auction.grpc.CreateAuctionRequest;
import org.kowal.auctionservice.entity.Auction;
import org.kowal.auctionservice.mapper.AuctionGrpcMapper;
import org.kowal.auctionservice.repository.AuctionRepository;
import org.kowal.enums.AuctionStatus;
import org.kowal.enums.AuctionType;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionGrpcMapper auctionGrpcMapper;

    public AuctionResponse createAuction(CreateAuctionRequest request){


        Auction auction = Auction.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .sellerId(request.getUserId())
                .startPrice(auctionGrpcMapper.mapDecimalToBigDecimal(request.getStartPrice()))
                .currentPrice(auctionGrpcMapper.mapDecimalToBigDecimal(request.getStartPrice()))
                .buyNowPrice(auctionGrpcMapper.mapDecimalToBigDecimal(request.getBuyNowPrice()))
                .minIncrement(auctionGrpcMapper.mapDecimalToBigDecimal(request.getMinIncrement()))
                .startTime(Instant.now())
                .endTime(auctionGrpcMapper.mapTimestampToInstant(request.getEndTime()))
                .auctionStatus(AuctionStatus.ACTIVE)
                .auctionType(AuctionType.valueOf(request.getAuctionType()))
                .createdAt(Instant.now())
                .build();

        Auction saved = auctionRepository.save(auction);
        return auctionGrpcMapper.mapAuctionToGrpcResponse(saved);
    }
}
