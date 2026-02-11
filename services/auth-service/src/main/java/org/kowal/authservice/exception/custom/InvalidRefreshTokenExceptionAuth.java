package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class InvalidRefreshTokenExceptionAuth extends AuthServiceException {
    public InvalidRefreshTokenExceptionAuth() {
        super("Invalid refresh token", Status.UNAUTHENTICATED);
    }
}
