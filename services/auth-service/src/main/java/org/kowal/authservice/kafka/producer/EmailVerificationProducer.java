package org.kowal.authservice.kafka.producer;

import lombok.AllArgsConstructor;

import org.kowal.event.grpc.EmailVerificationEvent;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailVerificationProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void send(EmailVerificationEvent event){

        byte[] bytes = event.toByteArray();

        kafkaTemplate.send(
                KafkaTopics.EMAIL_VERIFICATION,
                event.getUserId(),
                bytes
        );
    }
}
