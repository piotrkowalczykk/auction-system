package org.kowal.biddingservice.exception.custom;

import io.grpc.Status;

public class AuctionNotFoundException extends BiddingServiceException {
    public AuctionNotFoundException() {
        super("Auction not found", Status.NOT_FOUND);
    }
}
