package com.assignment.validator.services;

import com.assignment.validator.pojos.AuthCredentials;
import com.assignment.validator.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public boolean isAuthenticate(@NotNull final AuthCredentials authCredentials) {
		return accountRepository.findByUsernameAndAuthId(authCredentials.getUsername(), authCredentials.getAuthId()) != null;
	}
}
