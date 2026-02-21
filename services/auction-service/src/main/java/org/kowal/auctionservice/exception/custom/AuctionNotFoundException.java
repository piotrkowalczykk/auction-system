package org.kowal.auctionservice.exception.custom;

import io.grpc.Status;

public class AuctionNotFoundException extends AuctionServiceException {
    public AuctionNotFoundException(String auctionId) {
        super("Auction not found with id: " + auctionId, Status.NOT_FOUND);
    }
}
