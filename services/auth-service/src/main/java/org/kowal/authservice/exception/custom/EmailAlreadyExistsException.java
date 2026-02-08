package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailAlreadyExistsException extends ServiceException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email, Status.ALREADY_EXISTS);
    }
}
