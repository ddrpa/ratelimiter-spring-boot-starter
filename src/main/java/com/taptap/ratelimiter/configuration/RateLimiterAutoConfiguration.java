package com.taptap.ratelimiter.configuration;

import com.taptap.ratelimiter.core.RuleProvider;
import com.taptap.ratelimiter.core.RateLimitAspectHandler;
import com.taptap.ratelimiter.core.RateLimiterService;
import com.taptap.ratelimiter.web.RateLimitExceptionHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/3/16
 */
@Configuration
@ConditionalOnProperty(prefix = RateLimiterProperties.PREFIX, name = "enabled", havingValue = "true")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RateLimiterProperties.class)
@Import({RateLimitAspectHandler.class, RateLimitExceptionHandler.class})
public class RateLimiterAutoConfiguration {

    @Bean
    public RuleProvider bizKeyProvider() {
        return new RuleProvider();
    }

    @Bean
    public RateLimiterService rateLimiterInfoProvider(RedissonClient redissonClient) {
        return new RateLimiterService(redissonClient);
    }

}
