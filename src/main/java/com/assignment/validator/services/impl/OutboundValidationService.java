package com.assignment.validator.services.impl;

import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import com.assignment.validator.enums.ValidationStatus;
import com.assignment.validator.services.RateLimiterService;
import com.assignment.validator.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 12/07/20
 * @since 1.0.0
 */
@Service("outboundValidationService")
public class OutboundValidationService implements ValidationService {

	@Autowired
	private PhoneNumberService phoneNumberService;

	private final RateLimiterService rateLimiterService;

	private final ReactiveRedisOperations<String, ValidationRequest> redisBlockOperations;

	public OutboundValidationService(ReactiveRedisOperations<String, ValidationRequest> redisBlockOperations,
									 RateLimiterService rateLimiterService) {
		this.redisBlockOperations = redisBlockOperations;
		this.rateLimiterService = rateLimiterService;
	}

	@Override
	public Mono<ValidationResponse> validate(@NotNull String username, @NotNull final ValidationRequest validationRequest) {
		return rateLimiterService.apply("COUNT" + '_' + validationRequest.getFrom(), 2, 24, ChronoUnit.HOURS)
				.flatMap(isWithinLimit -> {
					if(!isWithinLimit) {
						return Mono.just(getValidationResponse("", ValidationStatus.INVALID, "limit reached for from " + validationRequest.getFrom()));
					}
					return phoneNumberService.isNumberValid(username, validationRequest.getFrom())
							.flatMap(isNumberValid -> {
								if(!isNumberValid) {
									return Mono.just(getValidationResponse("", ValidationStatus.INVALID, "to parameter not found"));
								}
								return redisBlockOperations.opsForValue().get("STOP" + '_' + validationRequest.getFrom() + '_' + validationRequest.getTo())
										.map(cachedValidationRequest -> ValidationResponse
												.builder()
												.status(ValidationStatus.INVALID)
												.message("")
												.error("sms from " + validationRequest.getFrom() + " to " + validationRequest.getTo() + " blocked by STOP request")
												.build())
										.defaultIfEmpty(getValidationResponse("outbound sms ok", ValidationStatus.VALID, ""));
							});
				});
	}

	@Override
	public Mono<Boolean> rateLimit(final ValidationRequest validationRequest) {
		return rateLimiterService.getCount("COUNT" + validationRequest.getFrom())
				.flatMap(count -> rateLimiterService.apply("COUNT" + '_' + validationRequest.getFrom(), 4, 24, ChronoUnit.HOURS));
	}

	private ValidationResponse getValidationResponse(final String message, final ValidationStatus status, final String errorMessage) {
		return ValidationResponse.builder()
				.message(message)
				.status(status)
				.error(errorMessage)
				.build();
	}
}
