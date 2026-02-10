package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class PasswordResetTokenNotFoundException extends ServiceException{
    public PasswordResetTokenNotFoundException() {
        super("Password reset token not found", Status.NOT_FOUND);
    }
}
