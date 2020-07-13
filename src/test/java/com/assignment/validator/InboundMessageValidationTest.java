package com.assignment.validator;

import com.assignment.validator.constants.CacheKeyConstants;
import com.assignment.validator.constants.Message;
import com.assignment.validator.dto.ValidationRequest;
import com.assignment.validator.enums.ValidationStatus;
import com.assignment.validator.services.CacheService;
import com.assignment.validator.services.impl.InboundValidationService;
import com.assignment.validator.services.impl.PhoneNumberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 13/07/20
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class InboundMessageValidationTest {

	@Mock
	private PhoneNumberService phoneNumberService;

	@Mock
	private CacheService<ValidationRequest> cacheService;

	@InjectMocks
	private InboundValidationService inboundValidationService;

	@Test
	public void stopMessageValid() {
		final String username = "u1";
		final String from = "1239876";
		final String to = "45884939";
		final String text = "STOP, ...";
		ReflectionTestUtils.setField(inboundValidationService, "stopRequestExpiryInHours", 1);
		final ValidationRequest validationRequest = ValidationRequest.builder().from(from).to(to).text(text).build();
		Mockito.when(phoneNumberService.isNumberValid(username, to)).thenReturn(Mono.just(true));
		Mockito.when(cacheService.set(Message.BLOCK_TOKEN + CacheKeyConstants.HYPHEN + validationRequest.getFrom() + CacheKeyConstants.HYPHEN + validationRequest.getTo(), validationRequest, Duration.of(1, ChronoUnit.HOURS), ValidationRequest.class))
				.thenReturn(Mono.just(true));
		StepVerifier.create(inboundValidationService.validate(username, validationRequest))
				.assertNext(validationResponse -> {
					assert null != validationResponse;
					assert null != validationResponse.getStatus();
					assert ValidationStatus.VALID == validationResponse.getStatus();
					assert StringUtils.isEmpty(validationResponse.getError());
					assert !StringUtils.isEmpty(validationResponse.getMessage());
					assert "inbound sms ok".equals(validationResponse.getMessage());
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void stopMessageInvalid_phoneNotExists() {
		final String username = "u1";
		final String from = "1239876";
		final String to = "45884939";
		final String text = "STOP, ...";
		ReflectionTestUtils.setField(inboundValidationService, "stopRequestExpiryInHours", 1);
		final ValidationRequest validationRequest = ValidationRequest.builder().from(from).to(to).text(text).build();
		Mockito.when(phoneNumberService.isNumberValid(username, to)).thenReturn(Mono.just(false));
		StepVerifier.create(inboundValidationService.validate(username, validationRequest))
				.assertNext(validationResponse -> {
					assert null != validationResponse;
					assert null != validationResponse.getStatus();
					assert ValidationStatus.INVALID == validationResponse.getStatus();
					assert !StringUtils.isEmpty(validationResponse.getError());
					assert "to parameter not found".equals(validationResponse.getError());
					assert StringUtils.isEmpty(validationResponse.getMessage());
				})
				.expectComplete()
				.verify();
	}
}
