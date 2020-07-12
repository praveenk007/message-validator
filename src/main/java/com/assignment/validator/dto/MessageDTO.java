package com.assignment.validator.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

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
@Builder
@Validated
public class MessageDTO {

	@Size(min = 6, max = 16)
	private String from;

	@Size(min = 6, max = 16)
	private String to;

	@Size(min = 1, max = 120)
	private String text;
}
