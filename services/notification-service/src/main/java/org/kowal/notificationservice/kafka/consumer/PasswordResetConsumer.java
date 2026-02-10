package org.kowal.notificationservice.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AllArgsConstructor;
import org.kowal.event.grpc.ResetPasswordEvent;
import org.kowal.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordResetConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "password-reset-topic", groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(byte[] bytes) throws InvalidProtocolBufferException {
        ResetPasswordEvent event = ResetPasswordEvent.parseFrom(bytes);

        emailService.sendPasswordResetEmail(
                event.getEmail(),
                event.getResetToken()
        );

    }
}
