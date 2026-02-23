package org.kowal.biddingservice.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.kowal.biddingservice.mapper.BiddingGrpcMapper;
import org.kowal.biddingservice.redis.cache.AuctionCacheManager;
import org.kowal.event.grpc.AuctionCreatedEvent;
import org.kowal.topic.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionCreatedConsumer {
    private final AuctionCacheManager auctionCacheManager;
    private final BiddingGrpcMapper biddingGrpcMapper;

    @KafkaListener(topics = KafkaTopics.AUCTION_CREATED, groupId = "bidding-service",
            containerFactory = "kafkaListenerContainerFactory")
        public void consume(byte[] bytes) throws InvalidProtocolBufferException {
        AuctionCreatedEvent event = AuctionCreatedEvent.parseFrom(bytes);

        auctionCacheManager.initializeAuction(
                event.getAuctionId(),
                biddingGrpcMapper.mapDecimalToBigDecimal(event.getStartPrice()),
                biddingGrpcMapper.mapDecimalToBigDecimal(event.getMinIncrement()),
                biddingGrpcMapper.mapDecimalToBigDecimal(event.getBuyNowPrice()),
                biddingGrpcMapper.mapTimestampToInstant(event.getEndTime()),
                event.getAuctionType());
    }
}
