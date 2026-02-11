package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailVerificationTokenAlreadyUsedExceptionAuth extends AuthServiceException {
    public EmailVerificationTokenAlreadyUsedExceptionAuth() {
        super("Email verification token already used", Status.ALREADY_EXISTS);
    }
}
