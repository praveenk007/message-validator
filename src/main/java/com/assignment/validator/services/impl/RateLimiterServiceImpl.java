package com.assignment.validator.services.impl;

import com.assignment.validator.services.CacheService;
import com.assignment.validator.services.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RateLimiterServiceImpl implements RateLimiterService {

	@Autowired
	private CacheService<Long> cacheService;

	@Override
	public Mono<Long> getCount(String key) {
		return cacheService.get(key, Long.class).defaultIfEmpty(0L);
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
		return cacheService.set(key, count, Duration.of(ttl, temporalUnit), Long.class);
	}

	private Mono<Boolean> updateCount(String key, Long count) {
		return cacheService.set(key, count, Long.class);
	}
}
