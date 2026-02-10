package org.kowal.authservice.kafka.producer;

import lombok.AllArgsConstructor;
import org.kowal.event.grpc.ResetPasswordEvent;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordResetProducer {
    private KafkaTemplate<String, byte[]> kafkaTemplate;

     public void send(ResetPasswordEvent event){
        kafkaTemplate.send(
                KafkaTopics.PASSWORD_RESET,
                event.getUserId(),
                event.toByteArray()
        );
    }
}
