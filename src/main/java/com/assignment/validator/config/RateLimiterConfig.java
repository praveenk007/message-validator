package com.assignment.validator.config;

import com.assignment.validator.services.RateLimiterService;
import com.assignment.validator.services.impl.RateLimiterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
public class RateLimiterConfig {

	@Autowired
	private RateLimiterServiceImpl rateLimiterService;

	@Bean
	public RateLimiterService rateLimiterService() {
		return rateLimiterService;
	}
}
