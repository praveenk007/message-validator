package com.assignment.validator.services.impl;

import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import com.assignment.validator.enums.ValidationStatus;
import com.assignment.validator.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

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

	private final ReactiveRedisOperations<String, ValidationRequest> redisBlockOperations;

	public OutboundValidationService(ReactiveRedisOperations<String, ValidationRequest> redisBlockOperations) {
		this.redisBlockOperations = redisBlockOperations;
	}

	@Override
	public Mono<ValidationResponse> validate(@NotNull String username, @NotNull final ValidationRequest validationRequest) {
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
							.defaultIfEmpty(ValidationResponse.builder().message("outbound sms ok").error("").build());
		});
	}

	private ValidationResponse getValidationResponse(final String message, final ValidationStatus status, final String errorMessage) {
		return ValidationResponse.builder()
				.message(message)
				.status(status)
				.error(errorMessage)
				.build();
	}
}
