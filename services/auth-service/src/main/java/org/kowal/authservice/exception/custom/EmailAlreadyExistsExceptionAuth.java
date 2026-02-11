package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailAlreadyExistsExceptionAuth extends AuthServiceException {
    public EmailAlreadyExistsExceptionAuth(String email) {
        super("Email already exists: " + email, Status.ALREADY_EXISTS);
    }
}
