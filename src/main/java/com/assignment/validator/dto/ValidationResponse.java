package com.assignment.validator.dto;

import com.assignment.validator.enums.ValidationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;

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
public class ValidationResponse {

	@JsonIgnore
	private ValidationStatus status;

	private String message;

	private String error;
}
