package org.kowal.authservice.exception.custom;

import io.grpc.Status;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    private final Status grpcStatus;
    public ServiceException(String message, Status grpcStatus) {
        super(message);
        this.grpcStatus = grpcStatus;
    }
}
