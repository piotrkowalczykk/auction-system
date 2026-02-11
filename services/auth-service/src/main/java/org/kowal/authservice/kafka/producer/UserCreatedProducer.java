package org.kowal.authservice.kafka.producer;

import lombok.AllArgsConstructor;
import org.kowal.event.grpc.UserCreatedEvent;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCreatedProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void send(UserCreatedEvent event){
        kafkaTemplate.send(
                KafkaTopics.USER_CREATED,
                event.getUserId(),
                event.toByteArray()
        );
    }
}
