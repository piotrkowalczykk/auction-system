package org.kowal.authservice.exception.custom;

import io.grpc.Status;

public class UserNotFoundException extends ServiceException{
    public UserNotFoundException(String email){
        super("User not found: " + email, Status.NOT_FOUND);
    }

    public UserNotFoundException(){
        super("User not found", Status.NOT_FOUND);
    }
}
