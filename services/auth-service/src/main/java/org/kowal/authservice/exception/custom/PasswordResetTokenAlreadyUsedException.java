package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class PasswordResetTokenAlreadyUsedException extends ServiceException {
    public PasswordResetTokenAlreadyUsedException() {
        super("Password reset token already used", Status.ALREADY_EXISTS);
    }
}
