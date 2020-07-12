package com.assignment.validator.services.impl;

import com.assignment.validator.services.RateLimiterService;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 13/07/20
 * @since 1.0.0
 */
@Service
public class RedisRateLimiterService implements RateLimiterService {

	private final ReactiveRedisOperations<String, Long> redisRateLimiterOperations;

	public RedisRateLimiterService(ReactiveRedisOperations<String, Long> redisRateLimiterOperations) {
		this.redisRateLimiterOperations = redisRateLimiterOperations;
	}

	@Override
	public Mono<Long> getCount(String key) {
		return redisRateLimiterOperations.opsForValue().get(key).defaultIfEmpty(0L);
	}

	@Override
	public Mono<Boolean> apply(String key, int maxCount, long ttl, TemporalUnit temporalUnit) {
		return getCount(key)
				.flatMap(count -> {
					if(count == 0L) {
						return updateCount(key, count + 1, ttl, temporalUnit);
					} else if(count == maxCount) {
						return Mono.just(false);
					}
					return updateCount(key, count + 1);
				});
	}

	private Mono<Boolean> updateCount(String key, Long count, long ttl, TemporalUnit temporalUnit) {
		return redisRateLimiterOperations.opsForValue().set(key, count, Duration.of(ttl, temporalUnit));
	}

	private Mono<Boolean> updateCount(String key, Long count) {
		return redisRateLimiterOperations.opsForValue().set(key, count);
	}
}
