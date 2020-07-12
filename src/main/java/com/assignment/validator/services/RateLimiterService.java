package com.assignment.validator.services;

import reactor.core.publisher.Mono;

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
public interface RateLimiterService {

	Mono<Long> getCount(String key);

	Mono<Boolean> apply(String key, int maxCount, long ttl, TemporalUnit temporalUnit);
}
