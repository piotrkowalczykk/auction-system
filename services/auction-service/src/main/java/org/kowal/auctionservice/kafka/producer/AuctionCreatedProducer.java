package org.kowal.auctionservice.kafka.producer;

import lombok.AllArgsConstructor;
import org.kowal.event.grpc.AuctionCreatedEvent;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuctionCreatedProducer {
    KafkaTemplate<String, byte[]> kafkaTemplate;

    public void send(AuctionCreatedEvent event){
        kafkaTemplate.send(
                KafkaTopics.AUCTION_CREATED,
                event.getAuctionId(),
                event.toByteArray()
        );
    }
}
