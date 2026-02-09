package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailVerificationTokenExpiredException extends ServiceException {
    public EmailVerificationTokenExpiredException() {
        super("Email verification token expired", Status.PERMISSION_DENIED);
    }
}
