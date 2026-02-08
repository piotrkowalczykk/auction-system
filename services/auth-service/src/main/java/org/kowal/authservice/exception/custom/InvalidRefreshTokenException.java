package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class InvalidRefreshTokenException extends ServiceException {
    public InvalidRefreshTokenException() {
        super("Invalid refresh token", Status.UNAUTHENTICATED);
    }
}
