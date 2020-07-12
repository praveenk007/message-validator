package com.assignment.validator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class ValidationRequest {

	@NotNull
	@Size(min = 6, max = 16)
	private String from;

	@NotNull
	@Size(min = 6, max = 16)
	private String to;

	@NotNull
	@Size(min = 1, max = 120)
	private String text;
}
