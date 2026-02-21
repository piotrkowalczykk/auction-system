package org.kowal.biddingservice.exception.custom;

import io.grpc.Status;
import lombok.Getter;

@Getter
public class BiddingServiceException extends RuntimeException{
    private final Status grpcStatus;
    public BiddingServiceException(String message, Status grpcStatus) {
        super(message);
        this.grpcStatus = grpcStatus;
    }
}
