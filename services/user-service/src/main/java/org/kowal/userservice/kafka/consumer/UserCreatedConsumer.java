package org.kowal.userservice.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AllArgsConstructor;
import org.kowal.event.grpc.UserCreatedEvent;
import org.kowal.topic.KafkaTopics;
import org.kowal.userservice.service.UserProfileService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCreatedConsumer {

    private final UserProfileService userProfileService;

    @KafkaListener(topics = KafkaTopics.USER_CREATED, groupId = "user-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(byte[] bytes) throws InvalidProtocolBufferException {
        UserCreatedEvent event = UserCreatedEvent.parseFrom(bytes);

        userProfileService.createUserProfile(
                event.getUserId(),
                event.getEmail()
        );
    }
}
