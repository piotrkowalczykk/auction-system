package org.kowal.biddingservice.service;

import lombok.AllArgsConstructor;
import org.kowal.bidding.grpc.PlaceBidRequest;
import org.kowal.bidding.grpc.PlaceBidResponse;
import org.kowal.biddingservice.entity.Bid;
import org.kowal.biddingservice.exception.custom.AuctionNotFoundException;
import org.kowal.biddingservice.exception.custom.BidTooLowException;
import org.kowal.biddingservice.kafka.producer.AuctionEndedProducer;
import org.kowal.biddingservice.mapper.BiddingGrpcMapper;
import org.kowal.biddingservice.redis.cache.AuctionCacheManager;
import org.kowal.biddingservice.repository.BidRepository;
import org.kowal.event.grpc.AuctionEndedEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Service
@AllArgsConstructor
public class BiddingService {

    private final AuctionCacheManager auctionCacheManager;
    private final BidRepository bidRepository;
    private final BiddingGrpcMapper biddingGrpcMapper;
    private final AuctionEndedProducer auctionEndedProducer;


    public PlaceBidResponse placeBid(PlaceBidRequest request){
        Map<Object, Object> auction =
                auctionCacheManager.getAuction(request.getAuctionId());

        if (auction.isEmpty())
            throw new AuctionNotFoundException();

        BigDecimal current = new BigDecimal((String) auction.get("currentPrice"));
        BigDecimal minIncrement = new BigDecimal((String) auction.get("minIncrement"));
        BigDecimal buyNowPrice = new BigDecimal((String) auction.get("buyNowPrice"));
        BigDecimal newAmount = biddingGrpcMapper.mapDecimalToBigDecimal(request.getAmount());

        if (newAmount.compareTo(current.add(minIncrement)) < 0) {
            throw new BidTooLowException(current, minIncrement, newAmount);
        }

        if(newAmount.compareTo(buyNowPrice) >= 0){
            finishAuctionImmediately(request.getAuctionId(), request.getBidderId(), buyNowPrice);
            return PlaceBidResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Buy now successful. Auction ended.")
                    .build();
        }

        auctionCacheManager.updateBid(
                request.getAuctionId(),
                newAmount,
                request.getBidderId()
        );

        bidRepository.save(
                Bid.builder()
                        .auctionId(request.getAuctionId())
                        .bidderId(request.getBidderId())
                        .amount(newAmount)
                        .createdAt(Instant.now())
                        .build()
        );

        return PlaceBidResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Bid placed successfully")
                .build();
    }

    private void finishAuctionImmediately(String auctionId, String winnerId, BigDecimal finalPrice){
        bidRepository.save(Bid.builder()
                .auctionId(auctionId)
                .bidderId(winnerId)
                .amount(finalPrice)
                .createdAt(Instant.now())
                .build()
        );

        auctionCacheManager.deleteAuction(auctionId);

        AuctionEndedEvent event = AuctionEndedEvent.newBuilder()
                .setAuctionId(auctionId)
                .setWinnerId(winnerId)
                .build();

        auctionEndedProducer.send(event);
    }
}
