package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class PasswordResetTokenExpiredException extends ServiceException {
    public PasswordResetTokenExpiredException(){
        super("Password reset token has expired", Status.PERMISSION_DENIED);
    }
}
