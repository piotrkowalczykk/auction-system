package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailVerificationTokenExpiredExceptionAuth extends AuthServiceException {
    public EmailVerificationTokenExpiredExceptionAuth() {
        super("Email verification token expired", Status.PERMISSION_DENIED);
    }
}
