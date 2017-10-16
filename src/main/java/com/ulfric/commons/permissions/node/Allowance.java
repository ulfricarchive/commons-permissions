package com.ulfric.commons.permissions.node;

public enum Allowance {

	ALLOWED,
	DENIED,
	UNDEFINED;

	public static Allowance valueOf(boolean value) {
		return value ? ALLOWED : DENIED;
	}

}
