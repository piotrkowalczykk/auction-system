package org.kowal.userservice.exception.mapper;

import io.grpc.Status;
import org.kowal.userservice.exception.custom.UserServiceException;

public class GrpcExceptionMapper {
    public static Status map(Throwable ex){
        if(ex instanceof UserServiceException userServiceException){
            return userServiceException.getGrpcStatus().withDescription(userServiceException.getMessage());
        }

        return Status.INTERNAL.withDescription("Internal server error" + (ex.getMessage() != null ? ": " + ex.getMessage() : ""));
    }
}

