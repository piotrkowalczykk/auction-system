package org.kowal.authservice.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kowal.authservice.kafka.producer.EmailVerificationProducer;
import org.kowal.event.grpc.EmailVerificationEvent;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationProducerTest {

    @Mock
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @InjectMocks
    private EmailVerificationProducer emailVerificationProducer;

    @Test
    void shouldSendEvent(){
        EmailVerificationEvent event = EmailVerificationEvent.newBuilder()
                .setUserId("123")
                .setEmail("test@test.com")
                .setVerificationToken("token")
                .build();

        emailVerificationProducer.send(event);
        verify(kafkaTemplate)
                .send(eq("email-verification-topic"), eq("123"), any(byte[].class));
    }

}
