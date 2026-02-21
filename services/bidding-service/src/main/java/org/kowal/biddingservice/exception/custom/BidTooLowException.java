package org.kowal.biddingservice.exception.custom;

import io.grpc.Status;

import java.math.BigDecimal;

public class BidTooLowException extends BiddingServiceException {
    public BidTooLowException(BigDecimal currentPrice, BigDecimal minIncrement, BigDecimal attemptedBid) {
        super(
                String.format(
                        "Bid %s is too low. With current price %s and min increment %s, you must bid at least %s.",
                        attemptedBid.toPlainString(),
                        currentPrice.toPlainString(),
                        minIncrement.toPlainString(),
                        currentPrice.add(minIncrement).toPlainString()
                ),
                Status.INVALID_ARGUMENT
        );
    }
}
