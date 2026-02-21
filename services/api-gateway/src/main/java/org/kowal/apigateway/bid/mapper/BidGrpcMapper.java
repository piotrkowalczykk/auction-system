package org.kowal.apigateway.bid.mapper;

import org.kowal.apigateway.bid.dto.BidResponseDto;
import org.kowal.bidding.grpc.PlaceBidResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BidGrpcMapper {

    public org.kowal.common.Decimal mapBigDecimalToDecimal(
            BigDecimal value
    ) {
        return org.kowal.common.Decimal.newBuilder()
                .setUnits(value.longValue())
                .setNanos(
                        value.remainder(BigDecimal.ONE)
                                .movePointRight(9)
                                .intValue()
                )
                .build();
    }

    public BidResponseDto toDto(PlaceBidResponse response) {

        return BidResponseDto.builder()
                .success(response.getSuccess())
                .message(response.getMessage())
                .build();
    }
}
