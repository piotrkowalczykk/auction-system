package org.kowal.biddingservice.redis.config;

import lombok.RequiredArgsConstructor;
import org.kowal.biddingservice.entity.Bid;
import org.kowal.biddingservice.kafka.producer.AuctionEndedProducer;
import org.kowal.biddingservice.repository.BidRepository;
import org.kowal.event.grpc.AuctionEndedEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisExpiredListener implements MessageListener {
    private final AuctionEndedProducer auctionEndedProducer;
    private final BidRepository bidRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if(!expiredKey.startsWith("auction:")){
            return;
        }

        String auctionId = expiredKey.replace("auction:", "");

        AuctionEndedEvent event = AuctionEndedEvent.newBuilder()
                .setAuctionId(auctionId)
                .setWinnerId(fetchWinnerFromDb(auctionId))
                .build();

        auctionEndedProducer.send(event);
    }

    private String fetchWinnerFromDb(String auctionId){
        return bidRepository.findTopByAuctionIdOrderByAmountDesc(auctionId)
                .map(Bid::getBidderId)
                .orElse("");
    }
}
