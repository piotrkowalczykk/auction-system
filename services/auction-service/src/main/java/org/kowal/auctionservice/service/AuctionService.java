package org.kowal.auctionservice.service;

import lombok.AllArgsConstructor;
import org.kowal.auction.grpc.AuctionResponse;
import org.kowal.auction.grpc.CreateAuctionRequest;
import org.kowal.auction.grpc.GetAllAuctionsRequest;
import org.kowal.auction.grpc.GetAllAuctionsResponse;
import org.kowal.auctionservice.entity.Auction;
import org.kowal.auctionservice.exception.custom.AuctionNotFoundException;
import org.kowal.auctionservice.kafka.producer.AuctionCreatedProducer;
import org.kowal.auctionservice.mapper.AuctionGrpcMapper;
import org.kowal.auctionservice.repository.AuctionRepository;
import org.kowal.enums.AuctionStatus;
import org.kowal.enums.AuctionType;
import org.kowal.event.grpc.AuctionCreatedEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionGrpcMapper auctionGrpcMapper;
    private final AuctionCreatedProducer auctionCreatedProducer;

    public AuctionResponse createAuction(CreateAuctionRequest request){

        AuctionType type = AuctionType.valueOf(request.getAuctionType());

        BigDecimal startPrice = auctionGrpcMapper.mapDecimalToBigDecimal(request.getStartPrice());
        BigDecimal minIncrement = null;
        BigDecimal buyNowPrice = null;

        switch(type){
            case AUCTION_ONLY -> {
                if(request.getMinIncrement() == null)
                    throw new IllegalArgumentException("Min increment required for BID auction");
                minIncrement = auctionGrpcMapper.mapDecimalToBigDecimal(request.getMinIncrement());

            }
            case BUY_NOW_ONLY -> {
                if(request.getBuyNowPrice() == null)
                    throw new IllegalArgumentException("Buy now price required");
                buyNowPrice = auctionGrpcMapper.mapDecimalToBigDecimal(request.getBuyNowPrice());
            }
            case AUCTION_WITH_BUY_NOW -> {
                if(request.getMinIncrement() == null || request.getBuyNowPrice() == null)
                    throw new IllegalArgumentException("Both minIncrement and buyNowPrice required");
                minIncrement = auctionGrpcMapper.mapDecimalToBigDecimal(request.getMinIncrement());
                buyNowPrice = auctionGrpcMapper.mapDecimalToBigDecimal(request.getBuyNowPrice());
            }
        }

        Auction auction = Auction.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .sellerId(request.getUserId())
                .startPrice(startPrice)
                .currentPrice(startPrice)
                .buyNowPrice(buyNowPrice)
                .minIncrement(minIncrement)
                .startTime(Instant.now())
                .endTime(auctionGrpcMapper.mapTimestampToInstant(request.getEndTime()))
                .auctionStatus(AuctionStatus.ACTIVE)
                .auctionType(type)
                .createdAt(Instant.now())
                .build();

        Auction saved = auctionRepository.save(auction);

        AuctionCreatedEvent event = AuctionCreatedEvent.newBuilder()
                .setAuctionId(saved.getId())
                .setSellerId(saved.getSellerId())
                .setStartPrice(auctionGrpcMapper.mapBigDecimalToDecimal(saved.getStartPrice()))
                .setMinIncrement(
                        saved.getMinIncrement() != null
                                ? auctionGrpcMapper.mapBigDecimalToDecimal(saved.getMinIncrement())
                                : null
                )
                .setBuyNowPrice(
                        saved.getBuyNowPrice() != null
                                ? auctionGrpcMapper.mapBigDecimalToDecimal(saved.getBuyNowPrice())
                                : null
                )
                .setEndTime(auctionGrpcMapper.mapInstantToTimestamp(saved.getEndTime()))
                .setAuctionType(saved.getAuctionType().name())
                .build();

        auctionCreatedProducer.send(event);

        return auctionGrpcMapper.mapAuctionToGrpcResponse(saved);
    }

    public void finishAuction(String auctionId){
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));

        auction.setAuctionStatus(AuctionStatus.ENDED);
        auctionRepository.save(auction);
    }

    public GetAllAuctionsResponse getAllAuctions() {

        List<Auction> auctions = auctionRepository.findAll();

        List<AuctionResponse> grpcResponses = auctions.stream()
                .map(auctionGrpcMapper::mapAuctionToGrpcResponse)
                .toList();

        return GetAllAuctionsResponse.newBuilder()
                .addAllAuctions(grpcResponses)
                .build();
    }

}
