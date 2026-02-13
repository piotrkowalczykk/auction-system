package org.kowal.auctionservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.auction.grpc.AuctionResponse;
import org.kowal.auction.grpc.AuctionServiceGrpc;
import org.kowal.auction.grpc.CreateAuctionRequest;
import org.kowal.auction.grpc.GetAuctionRequest;
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
    public void getAuction(GetAuctionRequest request, StreamObserver<AuctionResponse> responseObserver) {
        super.getAuction(request, responseObserver);
    }
}
