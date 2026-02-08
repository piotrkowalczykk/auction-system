package org.kowal.apigateway.exception.mapper;

import io.grpc.Status;
import org.springframework.http.HttpStatus;

public class GrpcToHttpStatusMapper {
    public static HttpStatus map(Status.Code code){
        return switch (code){
            case ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
            case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
            case PERMISSION_DENIED -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
