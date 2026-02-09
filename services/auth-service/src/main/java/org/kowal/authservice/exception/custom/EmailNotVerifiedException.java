package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailNotVerifiedException extends ServiceException {
    public EmailNotVerifiedException(String email) {
        super("Email not verified: " + email, Status.PERMISSION_DENIED);
    }
}
