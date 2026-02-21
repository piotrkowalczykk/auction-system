package org.kowal.auctionservice.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AllArgsConstructor;
import org.kowal.auctionservice.service.AuctionService;
import org.kowal.event.grpc.AuctionEndedEvent;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuctionEndedConsumer {

    private final AuctionService auctionService;

    @KafkaListener(topics = KafkaTopics.AUCTION_ENDED, groupId = "auction-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(byte[] bytes) throws InvalidProtocolBufferException {
        AuctionEndedEvent event = AuctionEndedEvent.parseFrom(bytes);

        auctionService.finishAuction(event.getAuctionId());
    }
}
