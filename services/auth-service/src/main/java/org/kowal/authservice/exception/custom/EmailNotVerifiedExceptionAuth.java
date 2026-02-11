package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class EmailNotVerifiedExceptionAuth extends AuthServiceException {
    public EmailNotVerifiedExceptionAuth(String email) {
        super("Email not verified: " + email, Status.PERMISSION_DENIED);
    }
}
