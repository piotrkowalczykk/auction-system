package org.kowal.biddingservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.bidding.grpc.BiddingServiceGrpc;
import org.kowal.bidding.grpc.PlaceBidRequest;
import org.kowal.bidding.grpc.PlaceBidResponse;
import org.kowal.biddingservice.service.BiddingService;

@GrpcService
@AllArgsConstructor
public class BidGrpcService extends BiddingServiceGrpc.BiddingServiceImplBase {

    private final BiddingService biddingService;

    @Override
    public void placeBid(PlaceBidRequest request, StreamObserver<PlaceBidResponse> responseObserver) {
        PlaceBidResponse response = biddingService.placeBid(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
