package org.kowal.authservice.exception.mapper;

import io.grpc.Status;
import org.kowal.authservice.exception.custom.*;

public class GrpcExceptionMapper {
    public static Status map(Throwable ex){
        if(ex instanceof AuthServiceException authServiceException){
            return authServiceException.getGrpcStatus().withDescription(authServiceException.getMessage());
        }

        return Status.INTERNAL.withDescription("Internal server error" + (ex.getMessage() != null ? ": " + ex.getMessage() : ""));
    }
}
