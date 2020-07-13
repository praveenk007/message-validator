package com.assignment.validator.services.impl;

import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.services.CacheOperations;
import com.assignment.validator.services.CacheService;
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
public class CacheBlockOperation<T> extends CacheOperations<T> {

	private CacheService<T> cacheService;

	public CacheBlockOperation(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	public Mono<Boolean> set(String key, T value, long ttl, ChronoUnit unit) {
		return null;
	}

	public Mono<Boolean> set(String key, T value) {
		return null;
	}

	public Mono<T> get(String key) {
		return null;
	}

}
