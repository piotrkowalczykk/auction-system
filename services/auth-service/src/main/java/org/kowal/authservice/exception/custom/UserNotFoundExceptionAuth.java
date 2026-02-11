package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class UserNotFoundExceptionAuth extends AuthServiceException {
    public UserNotFoundExceptionAuth(String email){
        super("User not found: " + email, Status.NOT_FOUND);
    }

    public UserNotFoundExceptionAuth(){
        super("User not found", Status.NOT_FOUND);
    }
}
