package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class InvalidCredentialsException extends ServiceException {
    public InvalidCredentialsException() {
        super("Invalid email or password", Status.UNAUTHENTICATED);
    }
}
