package com.assignment.validator.pojos;

import com.assignment.validator.enums.ValidationStatus;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 12/07/20
 * @since 1.0.0
 */
@Data
@Builder
public class ValidationResult {

	private ValidationStatus status;

	private String message;
}
