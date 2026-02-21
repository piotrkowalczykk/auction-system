package org.kowal.biddingservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.kowal.event.grpc.AuctionEndedEvent;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionEndedProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void send(AuctionEndedEvent event) {

        kafkaTemplate.send(
                KafkaTopics.AUCTION_ENDED,
                event.getAuctionId(),
                event.toByteArray()
        );
    }
}
