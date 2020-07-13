package com.assignment.validator.services;

import reactor.core.publisher.Mono;

import java.time.temporal.ChronoUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 13/07/20
 * @since 1.0.0
 */
public abstract class CacheOperations<T> {

	public abstract Mono<Boolean> set(String key, T value, long ttl, ChronoUnit unit);

	public abstract Mono<Boolean> set(String key, T value);

	public abstract Mono<T> get(String key);
}
