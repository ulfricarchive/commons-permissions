package com.ulfric.embargo.limit;

import com.ulfric.commons.value.Bean;

public class IntegerLimit extends Bean implements Limit {

	public static IntegerLimit of(int limit) {
		if (limit < 0) {
			throw new IllegalArgumentException("limit must be positive, was: " + limit);
		}

		return new IntegerLimit(limit);
	}

	private final int limit;

	private IntegerLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isWithinBounds(int ask) {
		return ask <= limit;
	}

	@Override
	public Limit and(Limit that) {
		if (that == StandardLimits.UNLIMITED) {
			return that;
		}

		if (that == StandardLimits.NONE) {
			return this;
		}

		if (that instanceof IntegerLimit) {
			IntegerLimit thatLimit = (IntegerLimit) that;
			return IntegerLimit.of(limit + thatLimit.limit);
		}

		throw new IllegalArgumentException("Could not merge " + this + " and " + that);
	}

}