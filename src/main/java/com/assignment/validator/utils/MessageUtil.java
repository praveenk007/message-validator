package com.assignment.validator.utils;

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
public class MessageUtil {

	public static boolean isStopMessage(@NotNull String message) {
		return message.trim().startsWith("STOP");
	}
}
