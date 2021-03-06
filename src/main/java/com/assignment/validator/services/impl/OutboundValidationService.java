package com.assignment.validator.services.impl;

import com.assignment.validator.constants.CacheKeyConstants;
import com.assignment.validator.constants.Message;
import com.assignment.validator.constants.ResponseMessage;
import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import com.assignment.validator.enums.ValidationStatus;
import com.assignment.validator.services.CacheService;
import com.assignment.validator.services.RateLimiterService;
import com.assignment.validator.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.temporal.ChronoUnit;

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

	@Value("${rateLimiter.outbound.maxCount}")
	private int maxCount;

	@Value("${rateLimiter.outbound.ttlInHours}")
	private int ttlInHours;

	@Autowired
	private PhoneNumberService phoneNumberService;

	private final RateLimiterService rateLimiterService;

	private final CacheService<ValidationRequest> cacheService;

	public OutboundValidationService(CacheService<ValidationRequest> cacheService,
									 RateLimiterService rateLimiterService) {
		this.cacheService = cacheService;
		this.rateLimiterService = rateLimiterService;
	}

	@Override
	public Mono<ValidationResponse> validate(@NotNull String username, @NotNull final ValidationRequest validationRequest) {
		return rateLimiterService.apply(CacheKeyConstants.COUNT + CacheKeyConstants.HYPHEN + validationRequest.getFrom(), maxCount, ttlInHours, ChronoUnit.HOURS)
				.flatMap(isWithinLimit -> {
					if(!isWithinLimit) {
						return Mono.just(getValidationResponse("", ValidationStatus.INVALID, "limit reached for from " + validationRequest.getFrom()));
					}
					return phoneNumberService.isNumberValid(username, validationRequest.getFrom())
							.flatMap(isNumberValid -> {
								if(!isNumberValid) {
									return Mono.just(getValidationResponse("", ValidationStatus.INVALID, "to parameter not found"));
								}
								return cacheService.get(Message.BLOCK_TOKEN + CacheKeyConstants.HYPHEN + validationRequest.getFrom() + '_' + validationRequest.getTo(), ValidationRequest.class)
										.map(cachedValidationRequest -> ValidationResponse
												.builder()
												.status(ValidationStatus.INVALID)
												.message("")
												.error("sms from " + validationRequest.getFrom() + " to " + validationRequest.getTo() + " blocked by STOP request")
												.build())
										.defaultIfEmpty(getValidationResponse(ResponseMessage.OUTBOUND_OK, ValidationStatus.VALID, ""));
							});
				});
	}

	@Override
	public Mono<Boolean> rateLimit(final ValidationRequest validationRequest) {
		return rateLimiterService.getCount(CacheKeyConstants.COUNT + validationRequest.getFrom())
				.flatMap(count -> rateLimiterService.apply(CacheKeyConstants.COUNT + CacheKeyConstants.HYPHEN + validationRequest.getFrom(), 4, 24, ChronoUnit.HOURS));
	}

	private ValidationResponse getValidationResponse(final String message, final ValidationStatus status, final String errorMessage) {
		return ValidationResponse.builder()
				.message(message)
				.status(status)
				.error(errorMessage)
				.build();
	}
}
