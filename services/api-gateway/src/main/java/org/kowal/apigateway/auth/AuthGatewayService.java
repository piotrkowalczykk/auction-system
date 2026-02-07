package org.kowal.apigateway.auth;

import org.kowal.apigateway.auth.dto.LoginRequestDto;
import org.kowal.apigateway.auth.dto.LoginResponseDto;
import org.kowal.apigateway.auth.dto.RegisterRequestDto;
import org.kowal.apigateway.auth.dto.RegisterResponseDto;
import org.kowal.apigateway.grpc.AuthGrpcClient;
import org.kowal.auth.grpc.LoginResponse;
import org.kowal.auth.grpc.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthGatewayService {
    private final AuthGrpcClient authGrpcClient;

    public AuthGatewayService(AuthGrpcClient authGrpcClient) {
        this.authGrpcClient = authGrpcClient;
     }

    public RegisterResponseDto register(RegisterRequestDto request) {

        RegisterResponse grpcResponse =
                authGrpcClient.register(request.getEmail(), request.getPassword());

        return new RegisterResponseDto(
                grpcResponse.getUserId(),
                grpcResponse.getSuccess()
        );
    }

    public LoginResponseDto login(LoginRequestDto request){
        LoginResponse grpcResponse = authGrpcClient.login(request.getEmail(), request.getPassword());

        return new LoginResponseDto(
                grpcResponse.getAccessToken(),
                grpcResponse.getRefreshToken()
        );
    }
}
