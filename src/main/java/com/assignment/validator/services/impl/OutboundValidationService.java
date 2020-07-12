package com.assignment.validator.services.impl;

import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import com.assignment.validator.services.ValidationService;
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

	@Override
	public Mono<ValidationResponse> validate(@NotNull String username, @NotNull final ValidationRequest validationRequest) {
		return Mono.empty();
	}
}
