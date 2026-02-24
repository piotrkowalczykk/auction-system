package org.kowal.auctionservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.auction.grpc.*;
import org.kowal.auctionservice.service.AuctionService;

@GrpcService
@AllArgsConstructor
public class AuctionGrpcService extends AuctionServiceGrpc.AuctionServiceImplBase {
    private final AuctionService auctionService;

    @Override
    public void createAuction(CreateAuctionRequest request, StreamObserver<AuctionResponse> responseObserver) {
        AuctionResponse response = auctionService.createAuction(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllAuctions(GetAllAuctionsRequest request, StreamObserver<GetAllAuctionsResponse> responseObserver) {
        GetAllAuctionsResponse response = auctionService.getAllAuctions();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
