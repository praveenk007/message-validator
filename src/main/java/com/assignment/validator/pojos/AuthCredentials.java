package com.assignment.validator.pojos;

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
public class AuthCredentials {

	private String username;

	private String authId;
}
