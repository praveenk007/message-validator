package com.assignment.validator.controllers;

import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.dto.ValidationResponse;
import com.assignment.validator.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 11/07/20
 * @since 1.0.0
 */
@RestController()
public class ValidationController {

	@Autowired
	@Qualifier("inboundValidationService")
	private ValidationService inboundValidationService;

	@Autowired
	@Qualifier("outboundValidationService")
	private ValidationService outboundValidationService;

	@PostMapping("/inbound/sms")
	public Mono<ResponseEntity<ValidationResponse>> inbound(@Valid @RequestBody ValidationRequest request, @RequestHeader Map<String, String> headers) {
		return inboundValidationService.validate(headers.get("username"), request).map(validationResponse -> ResponseEntity.status(HttpStatus.OK).body(validationResponse));
	}

	@PostMapping("/outbound/sms")
	public Mono<ValidationResponse> outbound(@Valid @RequestBody ValidationRequest request, @RequestHeader Map<String, String> headers) {
		return outboundValidationService.validate(headers.get("username"), request);
	}
}
