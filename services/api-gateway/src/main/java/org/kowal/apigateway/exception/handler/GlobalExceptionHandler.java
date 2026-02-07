package org.kowal.apigateway.exception.handler;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.kowal.apigateway.exception.custom.JwtAuthenticationException;
import org.kowal.apigateway.exception.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGrpcException(StatusRuntimeException ex) {
        Status.Code code = ex.getStatus().getCode();
        HttpStatus httpStatus = switch (code){
            case ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
            case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus().getDescription(),
                httpStatus.value(),
                Instant.now()
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
