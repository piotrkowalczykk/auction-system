package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class InvalidCredentialsExceptionAuth extends AuthServiceException {
    public InvalidCredentialsExceptionAuth() {
        super("Invalid email or password", Status.UNAUTHENTICATED);
    }
}
