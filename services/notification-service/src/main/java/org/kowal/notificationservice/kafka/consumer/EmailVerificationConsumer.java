package org.kowal.notificationservice.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AllArgsConstructor;
import org.kowal.event.grpc.EmailVerificationEvent;
import org.kowal.notificationservice.service.EmailService;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailVerificationConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = KafkaTopics.EMAIL_VERIFICATION, groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(byte[] bytes) throws InvalidProtocolBufferException {

        EmailVerificationEvent event = EmailVerificationEvent.parseFrom(bytes);

        emailService.sendVerificationEmail(
                event.getEmail(),
                event.getVerificationToken()
        );
    }
}
