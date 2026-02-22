package org.kowal.biddingservice.redis.cache;

import lombok.RequiredArgsConstructor;
import org.kowal.enums.AuctionType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AuctionCacheManager {
    private final StringRedisTemplate redisTemplate;

    public void initializeAuction(String auctionId, BigDecimal startPrice, BigDecimal minIncrement, BigDecimal buyNowPrice, Instant endTime, AuctionType auctionType){
        String key = buildKey(auctionId);
        redisTemplate.opsForHash().put(key, "currentPrice", startPrice.toString());
        redisTemplate.opsForHash().put(key, "minIncrement", minIncrement.toString());
        redisTemplate.opsForHash().put(key, "buyNowPrice", buyNowPrice.toString());
        redisTemplate.opsForHash().put(key, "winnerId", "");
        redisTemplate.opsForHash().put(key, "endTime", String.valueOf(endTime.getEpochSecond()));
        redisTemplate.opsForHash().put(key, "auctionType", auctionType.name());

        long ttl = Duration.between(Instant.now(), endTime).getSeconds();
        redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
    }

    public Map<Object, Object> getAuction(String auctionId){
        return redisTemplate.opsForHash().entries(buildKey(auctionId));
    }

    public void updateBid(String auctionId, BigDecimal amount, String bidderId){
        String key = buildKey(auctionId);
        redisTemplate.opsForHash().put(key, "currentPrice", amount.toString());
        redisTemplate.opsForHash().put(key, "winnerId", bidderId);
    }

    private String buildKey(String auctionId) {
        return "auction:" + auctionId;
    }

    public void deleteAuction(String auctionId){
        redisTemplate.delete(buildKey(auctionId));
    }
}
