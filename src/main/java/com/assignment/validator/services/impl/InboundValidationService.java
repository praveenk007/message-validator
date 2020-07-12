package com.assignment.validator.services.impl;

import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import com.assignment.validator.enums.ValidationStatus;
import com.assignment.validator.services.ValidationService;
import com.assignment.validator.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.Duration;
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
@Service("inboundValidationService")
public class InboundValidationService implements ValidationService {

	@Autowired
	private PhoneNumberService phoneNumberService;

	@Value("${cache.stopRequestExpiryInHours}")
	private long stopRequestExpiryInHours;

	private final ReactiveRedisOperations<String, ValidationRequest> redisBlockOperations;

	public InboundValidationService(ReactiveRedisOperations<String, ValidationRequest> redisBlockOperations) {
		this.redisBlockOperations = redisBlockOperations;
	}

	@Override
	public Mono<ValidationResponse> validate(@NotNull String username, @NotNull final ValidationRequest validationRequest) {
		return phoneNumberService.isNumberValid(username, validationRequest.getTo()).map(isNumberValid -> {
			if(!isNumberValid) {
				return getValidationResponse("", ValidationStatus.INVALID, "to parameter not found");
			}
			if(MessageUtil.isStopMessage(validationRequest.getText())) {
				redisBlockOperations.opsForValue().set(validationRequest.getTo(), validationRequest, Duration.of(stopRequestExpiryInHours, ChronoUnit.HOURS)).subscribe();
				redisBlockOperations.opsForValue().set(validationRequest.getFrom(), validationRequest, Duration.of(stopRequestExpiryInHours, ChronoUnit.HOURS)).subscribe();
			}
			return getValidationResponse("inbound sms ok", ValidationStatus.VALID, "");
		});
	}

	private ValidationResponse getValidationResponse(String message, ValidationStatus status, String errorMessage) {
		return ValidationResponse.builder()
				.message(message)
				.status(status)
				.error(errorMessage)
				.build();
	}
}
