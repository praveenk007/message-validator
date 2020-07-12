package com.assignment.validator.services;

import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 12/07/20
 * @since 1.0.0
 */
@Service
public interface ValidationService {

	Mono<ValidationResponse> validate(String username, ValidationRequest validationRequest);
}
