package org.kowal.auctionservice.exception.custom;

import io.grpc.Status;
import lombok.Getter;

@Getter
public class AuctionServiceException extends RuntimeException{
    private final Status grpcStatus;
    public AuctionServiceException(String message, Status grpcStatus) {
        super(message);
        this.grpcStatus = grpcStatus;
    }
}
