package org.kowal.apigateway.auth;

import org.kowal.apigateway.auth.dto.RegisterRequestDto;
import org.kowal.apigateway.auth.dto.RegisterResponseDto;
import org.kowal.apigateway.grpc.AuthGrpcClient;
import org.kowal.auth.grpc.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthGrpcClient authGrpcClient;

    public AuthService(AuthGrpcClient authGrpcClient) {
        this.authGrpcClient = authGrpcClient;
     }

    public RegisterResponseDto register(RegisterRequestDto dto) {

        RegisterResponse grpcResponse =
                authGrpcClient.register(dto.getEmail(), dto.getPassword());

        return new RegisterResponseDto(
                grpcResponse.getUserId(),
                grpcResponse.getSuccess()
        );
    }
}
