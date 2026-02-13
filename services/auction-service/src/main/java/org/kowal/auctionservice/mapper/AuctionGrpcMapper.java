package org.kowal.auctionservice.mapper;

import com.google.protobuf.Timestamp;
import org.kowal.auction.grpc.AuctionResponse;
import org.kowal.auction.grpc.Decimal;
import org.kowal.auctionservice.entity.Auction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class AuctionGrpcMapper {

    public BigDecimal mapDecimalToBigDecimal(Decimal decimal) {

        return BigDecimal.valueOf(decimal.getUnits())
                .add(
                        BigDecimal.valueOf(decimal.getNanos(), 9)
                );
    }

    public Instant mapTimestampToInstant(Timestamp timestamp) {

        if (timestamp == null) {
            return null;
        }

        return Instant.ofEpochSecond(
                timestamp.getSeconds(),
                timestamp.getNanos()
        );
    }

    public Decimal mapBigDecimalToDecimal(BigDecimal value) {

        return Decimal.newBuilder()
                .setUnits(value.longValue())
                .setNanos(
                        value.remainder(BigDecimal.ONE)
                                .movePointRight(9)
                                .intValue()
                )
                .build();
    }

    public Timestamp mapInstantToTimestamp(Instant instant) {

        if (instant == null) {
            return Timestamp.getDefaultInstance();
        }

        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    public AuctionResponse mapAuctionToGrpcResponse(Auction auction) {

        return AuctionResponse.newBuilder()
                .setId(auction.getId())
                .setTitle(auction.getTitle())
                .setDescription(auction.getDescription())
                .setSellerId(auction.getSellerId())

                .setStartPrice(mapBigDecimalToDecimal(auction.getStartPrice()))
                .setCurrentPrice(mapBigDecimalToDecimal(auction.getCurrentPrice()))
                .setBuyNowPrice(mapBigDecimalToDecimal(auction.getBuyNowPrice()))
                .setMinIncrement(mapBigDecimalToDecimal(auction.getMinIncrement()))
                .setStartTime(
                        mapInstantToTimestamp(auction.getStartTime())
                )
                .setEndTime(
                        mapInstantToTimestamp(auction.getEndTime())
                )
                .setCreatedAt(
                        mapInstantToTimestamp(auction.getCreatedAt())
                )

                .setAuctionStatus(
                        auction.getAuctionStatus().name()
                )
                .setAuctionType(
                        auction.getAuctionType().name()
                )
                .build();
    }
}

