package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class PasswordResetTokenNotFoundExceptionAuth extends AuthServiceException {
    public PasswordResetTokenNotFoundExceptionAuth() {
        super("Password reset token not found", Status.NOT_FOUND);
    }
}
