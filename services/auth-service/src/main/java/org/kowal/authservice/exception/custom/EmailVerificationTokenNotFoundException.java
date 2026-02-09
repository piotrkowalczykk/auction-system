package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailVerificationTokenNotFoundException extends ServiceException {
    public EmailVerificationTokenNotFoundException() {
        super("Email verification token not found", Status.NOT_FOUND);
    }
}
