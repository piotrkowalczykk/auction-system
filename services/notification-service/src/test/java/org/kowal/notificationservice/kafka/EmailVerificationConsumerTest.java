package org.kowal.notificationservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kowal.event.grpc.EmailVerificationEvent;
import org.kowal.notificationservice.kafka.consumer.EmailVerificationConsumer;
import org.kowal.notificationservice.service.EmailService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailVerificationConsumer consumer;


    @Test
    public void shouldSendVerificationEmail() throws InvalidProtocolBufferException {
        EmailVerificationEvent event = EmailVerificationEvent.newBuilder()
                .setEmail("test@test.com")
                .setUserId("123")
                .setVerificationToken("token")
                .build();

        byte[] bytes = event.toByteArray();

        consumer.consume(bytes);

        verify(emailService)
                .sendVerificationEmail("test@test.com", "token");
    }
}
