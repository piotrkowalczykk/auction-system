package org.kowal.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String message;
    private final int status;
    private final Instant timestamp;
}
