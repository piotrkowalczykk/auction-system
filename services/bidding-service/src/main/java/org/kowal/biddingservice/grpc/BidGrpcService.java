package org.kowal.biddingservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.bidding.grpc.*;
import org.kowal.biddingservice.mapper.BiddingGrpcMapper;
import org.kowal.biddingservice.redis.cache.AuctionCacheManager;
import org.kowal.biddingservice.service.BiddingService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@GrpcService
@AllArgsConstructor
public class BidGrpcService extends BiddingServiceGrpc.BiddingServiceImplBase {

    private final BiddingService biddingService;
    private final AuctionCacheManager auctionCacheManager;
    private final BiddingGrpcMapper biddingGrpcMapper;
    private final Map<String, CopyOnWriteArrayList<StreamObserver<AuctionServerEvent>>> sessions
            = new ConcurrentHashMap<>();

    @Override
    public void placeBid(PlaceBidRequest request, StreamObserver<PlaceBidResponse> responseObserver) {
        PlaceBidResponse response = biddingService.placeBid(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<AuctionClientEvent> liveAuction(
            StreamObserver<AuctionServerEvent> responseObserver) {

        return new StreamObserver<>() {
            private String auctionId;

            @Override
            public void onNext(AuctionClientEvent event) {

                auctionId = event.getAuctionId();


                if (event.hasJoin()) {

                    sessions
                            .computeIfAbsent(auctionId, k -> new CopyOnWriteArrayList<>())
                            .add(responseObserver);

                    return;
                }

                if (event.hasBid()) {

                    biddingService.placeBid(event.getBid());

                    Map<Object, Object> auction =
                            auctionCacheManager.getAuction(auctionId);

                    AuctionServerEvent serverEvent =
                            AuctionServerEvent.newBuilder()
                                    .setAuctionId(auctionId)
                                    .setUpdate(
                                            AuctionUpdate.newBuilder()
                                                    .setCurrentPrice(
                                                            biddingGrpcMapper.mapBigDecimalToDecimal(
                                                                    new BigDecimal(
                                                                            (String) auction.get("currentPrice")
                                                                    )
                                                            )
                                                    )
                                                    .setLeaderId(
                                                            (String) auction.get("winnerId")
                                                    )
                                                    .build()
                                    )
                                    .build();

                    broadcast(auctionId, serverEvent);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                cleanup();
            }

            @Override
            public void onCompleted() {
                cleanup();
                responseObserver.onCompleted();
            }

            private void cleanup() {
                if (auctionId != null) {
                    CopyOnWriteArrayList<StreamObserver<AuctionServerEvent>> observers =
                            sessions.get(auctionId);

                    if (observers != null) {
                        observers.remove(responseObserver);
                    }
                }
            }
        };
    }

    private void broadcast(String auctionId, AuctionServerEvent event) {

        CopyOnWriteArrayList<StreamObserver<AuctionServerEvent>> observers =
                sessions.get(auctionId);

        if (observers == null) return;

        for (StreamObserver<AuctionServerEvent> observer : observers) {
            try {
                observer.onNext(event);
            } catch (Exception e) {
                observers.remove(observer);
            }
        }
    }
}
