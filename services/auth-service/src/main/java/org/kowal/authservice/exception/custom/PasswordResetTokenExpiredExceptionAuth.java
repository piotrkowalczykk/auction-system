package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class PasswordResetTokenExpiredExceptionAuth extends AuthServiceException {
    public PasswordResetTokenExpiredExceptionAuth(){
        super("Password reset token has expired", Status.PERMISSION_DENIED);
    }
}
