package org.kowal.authservice.exception.mapper;

import io.grpc.Status;
import org.kowal.authservice.exception.custom.*;

public class GrpcExceptionMapper {
    public static Status map(Throwable ex){
        if(ex instanceof ServiceException serviceException){
            return serviceException.getGrpcStatus().withDescription(serviceException.getMessage());
        }

        return Status.INTERNAL.withDescription("Internal server error" + (ex.getMessage() != null ? ": " + ex.getMessage() : ""));
    }
}
