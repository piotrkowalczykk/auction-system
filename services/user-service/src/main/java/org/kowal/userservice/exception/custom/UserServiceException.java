package org.kowal.userservice.exception.custom;

import io.grpc.Status;
import lombok.Getter;

@Getter
public class UserServiceException extends RuntimeException {
    private final Status grpcStatus;
    public UserServiceException(String message, Status grpcStatus) {
        super(message);
        this.grpcStatus = grpcStatus;
    }
}
