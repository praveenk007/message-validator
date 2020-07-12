package com.assignment.validator.services.impl;

import com.assignment.validator.models.PhoneNumber;
import com.assignment.validator.repositories.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

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
public class PhoneNumberService {

	private static final Scheduler SCHEDULER = Schedulers.newElastic("phone-number-service");

	@Autowired
	private PhoneNumberRepository phoneNumberRepository;

	public Mono<Boolean> isNumberValid(final String username, final String number) {
		return Mono.fromSupplier(() -> {
			final PhoneNumber phoneNumber = phoneNumberRepository.findByNumber(number);
			return phoneNumber != null && phoneNumber.getAccount() != null && phoneNumber.getAccount().getUsername().equalsIgnoreCase(username);
		}).subscribeOn(SCHEDULER);
	}
}
