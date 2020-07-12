package com.assignment.validator.repositories;

import com.assignment.validator.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Account findByUsernameAndAuthId(String username, String authId);
}
