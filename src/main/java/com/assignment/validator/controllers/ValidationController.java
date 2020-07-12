package com.assignment.validator.controllers;

import com.assignment.validator.dto.MessageDTO;
import com.assignment.validator.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
	private ValidationService validationService;

	@PostMapping("/inbound/sms")
	public Mono<Object> inbound(@Valid @RequestBody MessageDTO message) {
		validationService.validateInboundMessage();
		return Mono.empty();
	}

	@PostMapping("/outbound/sms")
	public Mono<Object> outbound(@Valid @RequestBody MessageDTO message) {
		validationService.validateOutboundMessage();
		return Mono.empty();
	}
}
