package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailVerificationTokenNotFoundExceptionAuth extends AuthServiceException {
    public EmailVerificationTokenNotFoundExceptionAuth() {
        super("Email verification token not found", Status.NOT_FOUND);
    }
}
