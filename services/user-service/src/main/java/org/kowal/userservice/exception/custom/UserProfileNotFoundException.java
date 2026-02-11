package org.kowal.userservice.exception.custom;

import io.grpc.Status;

public class UserProfileNotFoundException extends UserServiceException{
    public UserProfileNotFoundException() {
        super("User profile not found", Status.NOT_FOUND);
    }
}
