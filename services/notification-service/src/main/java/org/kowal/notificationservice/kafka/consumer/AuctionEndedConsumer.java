package org.kowal.notificationservice.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AllArgsConstructor;
import org.kowal.event.grpc.AuctionEndedEvent;
import org.kowal.notificationservice.grpc.UserGrpcClient;
import org.kowal.notificationservice.service.EmailService;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuctionEndedConsumer {

    private final EmailService emailService;
    private final UserGrpcClient userGrpcClient;

    @KafkaListener(topics = KafkaTopics.AUCTION_ENDED, groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(byte[] bytes) throws InvalidProtocolBufferException {

        AuctionEndedEvent event = AuctionEndedEvent.parseFrom(bytes);

        String email = userGrpcClient.getUserEmail(event.getWinnerId());

        emailService.sendAuctionWinnerEmail(email, event.getAuctionId());
    }
}
