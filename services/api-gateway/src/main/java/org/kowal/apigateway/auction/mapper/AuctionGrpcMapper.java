package org.kowal.apigateway.auction.mapper;

import com.google.protobuf.Timestamp;
import org.kowal.apigateway.auction.dto.AuctionResponseDto;
import org.kowal.auction.grpc.AuctionResponse;
import org.kowal.auction.grpc.Decimal;
import org.kowal.enums.AuctionStatus;
import org.kowal.enums.AuctionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class AuctionGrpcMapper {
    public AuctionResponseDto mapAuctionResponseToAuctionResponseDto(AuctionResponse grpcResponse){
        return AuctionResponseDto.builder()
                .id(grpcResponse.getId())
                .title(grpcResponse.getTitle())
                .description(grpcResponse.getDescription())
                .sellerId(grpcResponse.getSellerId())
                .startPrice(mapDecimalToBigDecimal(grpcResponse.getStartPrice()))
                .currentPrice(mapDecimalToBigDecimal(grpcResponse.getCurrentPrice()))
                .buyNowPrice(mapDecimalToBigDecimal(grpcResponse.getBuyNowPrice()))
                .minIncrement(mapDecimalToBigDecimal(grpcResponse.getMinIncrement()))
                .startTime(mapTimestampToInstant(grpcResponse.getStartTime()))
                .endTime(mapTimestampToInstant(grpcResponse.getEndTime()))
                .auctionStatus(mapGrpcAuctionStatusToDomain(grpcResponse.getAuctionStatus()))
                .auctionType(mapGrpcAuctionTypeToDomain(grpcResponse.getAuctionType()))
                .createdAt(mapTimestampToInstant(grpcResponse.getCreatedAt()))
                .build();
    }

    private BigDecimal mapDecimalToBigDecimal(Decimal decimal) {
        if (decimal == null) {
            return null;
        }

        return BigDecimal.valueOf(decimal.getUnits())
                .add(BigDecimal.valueOf(decimal.getNanos(), 9));
    }

    private AuctionStatus mapGrpcAuctionStatusToDomain(String grpcAuctionStatus) {
        try {
            return AuctionStatus.valueOf(grpcAuctionStatus);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Unknown auction status");
        }
    }

    private AuctionType mapGrpcAuctionTypeToDomain(String grpcAuctionType){
        try {
            return AuctionType.valueOf(grpcAuctionType);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Unknown auction type");
        }
    }

    public Decimal mapBigDecimalToDecimal(BigDecimal value) {
        if (value == null) {
            return Decimal.getDefaultInstance();
        }

        long units = value.longValue();

        int nanos = value.subtract(BigDecimal.valueOf(units))
                .movePointRight(9)
                .intValue();

        return Decimal.newBuilder()
                .setUnits(units)
                .setNanos(nanos)
                .build();
    }

    public Timestamp mapInstantToTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    private Instant mapTimestampToInstant(Timestamp timestamp) {
        if (timestamp == null || (timestamp.getSeconds() == 0 && timestamp.getNanos() == 0)) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
