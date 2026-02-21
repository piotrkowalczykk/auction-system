package org.kowal.biddingservice.mapper;

import com.google.protobuf.Timestamp;
import org.kowal.common.Decimal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class BiddingGrpcMapper {
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
}
