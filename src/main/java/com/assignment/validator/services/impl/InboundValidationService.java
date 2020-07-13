package com.assignment.validator.services.impl;

import com.assignment.validator.constants.CacheKeyConstants;
import com.assignment.validator.constants.Message;
import com.assignment.validator.constants.ResponseMessage;
import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import com.assignment.validator.enums.ValidationStatus;
import com.assignment.validator.services.CacheService;
import com.assignment.validator.services.ValidationService;
import com.assignment.validator.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Value;
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

	private final PhoneNumberService phoneNumberService;

	@Value("${cache.stopRequestExpiryInHours}")
	private long stopRequestExpiryInHours;

	private final CacheService<ValidationRequest> cacheService;

	public InboundValidationService(CacheService<ValidationRequest> cacheService, PhoneNumberService phoneNumberService) {
		this.cacheService = cacheService;
		this.phoneNumberService = phoneNumberService;
	}

	@Override
	public Mono<ValidationResponse> validate(@NotNull String username, @NotNull final ValidationRequest validationRequest) {
		return phoneNumberService.isNumberValid(username, validationRequest.getTo()).map(isNumberValid -> {
			if(!isNumberValid) {
				return getValidationResponse("", ValidationStatus.INVALID, "to parameter not found");
			}
			if(MessageUtil.isStopMessage(validationRequest.getText())) {
				cacheService.set(Message.BLOCK_TOKEN + CacheKeyConstants.HYPHEN + validationRequest.getFrom() + CacheKeyConstants.HYPHEN + validationRequest.getTo(), validationRequest, Duration.of(stopRequestExpiryInHours, ChronoUnit.HOURS), ValidationRequest.class).subscribe();
			}
			return getValidationResponse(ResponseMessage.INBOUND_OK, ValidationStatus.VALID, "");
		});
	}

	@Override
	public Mono<Boolean> rateLimit(ValidationRequest validationRequest) {
		throw new UnsupportedOperationException("Operation not supported");
	}

	private ValidationResponse getValidationResponse(String message, ValidationStatus status, String errorMessage) {
		return ValidationResponse.builder()
				.message(message)
				.status(status)
				.error(errorMessage)
				.build();
	}
}
