package org.kowal.authservice.exception.custom;

import io.grpc.Status;
import lombok.Getter;

@Getter
public class AuthServiceException extends RuntimeException{
    private final Status grpcStatus;
    public AuthServiceException(String message, Status grpcStatus) {
        super(message);
        this.grpcStatus = grpcStatus;
    }
}
