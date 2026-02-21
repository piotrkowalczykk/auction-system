package org.kowal.biddingservice.service;

import lombok.AllArgsConstructor;
import org.kowal.bidding.grpc.PlaceBidRequest;
import org.kowal.bidding.grpc.PlaceBidResponse;
import org.kowal.biddingservice.entity.Bid;
import org.kowal.biddingservice.exception.custom.AuctionNotFoundException;
import org.kowal.biddingservice.exception.custom.BidTooLowException;
import org.kowal.biddingservice.mapper.BiddingGrpcMapper;
import org.kowal.biddingservice.redis.cache.AuctionCacheManager;
import org.kowal.biddingservice.repository.BidRepository;
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


    public PlaceBidResponse placeBid(PlaceBidRequest request){
        Map<Object, Object> auction =
                auctionCacheManager.getAuction(request.getAuctionId());

        if (auction.isEmpty())
            throw new AuctionNotFoundException();

        BigDecimal current = new BigDecimal((String) auction.get("currentPrice"));
        BigDecimal minIncrement = new BigDecimal((String) auction.get("minIncrement"));
        BigDecimal newAmount = biddingGrpcMapper.mapDecimalToBigDecimal(request.getAmount());

        if (newAmount.compareTo(current.add(minIncrement)) < 0) {
            throw new BidTooLowException(current, minIncrement, newAmount);
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
}
