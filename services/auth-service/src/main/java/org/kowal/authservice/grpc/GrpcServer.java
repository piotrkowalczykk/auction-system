package org.kowal.authservice.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
    public static void main(String[] args) throws Exception{
        Server server = ServerBuilder
                .forPort(9091)
                .addService(new AuthGrpcService())
                .build();

        server.start();
        System.out.println("Auth gRPC server started on port 9091");
        server.awaitTermination();
    }
}
