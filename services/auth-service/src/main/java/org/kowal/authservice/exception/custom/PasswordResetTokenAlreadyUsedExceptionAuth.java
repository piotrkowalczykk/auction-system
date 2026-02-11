package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class PasswordResetTokenAlreadyUsedExceptionAuth extends AuthServiceException {
    public PasswordResetTokenAlreadyUsedExceptionAuth() {
        super("Password reset token already used", Status.ALREADY_EXISTS);
    }
}
