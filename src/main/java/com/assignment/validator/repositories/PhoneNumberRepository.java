package com.assignment.validator.repositories;

import com.assignment.validator.models.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 12/07/20
 * @since 1.0.0
 */
@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

	PhoneNumber findByNumber(String number);
}
