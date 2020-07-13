package com.assignment.validator.config;

import com.assignment.validator.services.CacheService;
import com.assignment.validator.services.cache.redis.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 13/07/20
 * @since 1.0.0
 */
@Configuration
public class CacheConfig {

	@Autowired
	private RedisCacheService redisCacheService;

	public CacheService cacheService() {
		return redisCacheService;
	}
}
