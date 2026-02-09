package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailVerificationTokenAlreadyUsedException extends ServiceException {
    public EmailVerificationTokenAlreadyUsedException() {
        super("Email verification token already used", Status.ALREADY_EXISTS);
    }
}
