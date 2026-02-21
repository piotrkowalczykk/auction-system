package org.kowal.biddingservice.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisConfig {
    @Bean
    public StringRedisTemplate redisTemplate(
            RedisConnectionFactory factory) {

        return new StringRedisTemplate(factory);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory factory,
            RedisExpiredListener listener
    ) {
        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(factory);

        container.addMessageListener(
                listener,
                new PatternTopic("__keyevent@*__:expired")
        );

        return container;
    }
}
