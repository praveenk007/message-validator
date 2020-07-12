package com.assignment.validator.config;

import com.assignment.validator.dto.ValidationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 12/07/20
 * @since 1.0.0
 */
@Configuration
public class RedisBeans {

	@Value("${cache.redis.host}")
	private String host;

	@Value("${cache.redis.port}")
	private int port;

	@Bean
	LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
	}

	@Bean
	ReactiveRedisOperations<String, ValidationRequest> redisBlockOperations(ReactiveRedisConnectionFactory redisConnectionFactory) {
		final Jackson2JsonRedisSerializer<ValidationRequest> serializer = new Jackson2JsonRedisSerializer<>(ValidationRequest.class);
		final RedisSerializationContext.RedisSerializationContextBuilder<String, ValidationRequest> builder =
				RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
		final RedisSerializationContext<String, ValidationRequest> context = builder.value(serializer).build();
		return new ReactiveRedisTemplate<>(redisConnectionFactory, context);
	}
}
