package com.ulfric.commons.permissions.limit;

import com.ulfric.commons.value.Bean;

public class IntegerLimit extends Bean implements Limit {

	private static final IntegerLimit ONE = new IntegerLimit(1);

	public static IntegerLimit of(int limit) {
		if (limit < 1) {
			throw new IllegalArgumentException("limit must be positive, was: " + limit);
		}

		if (limit == 1) {
			return ONE;
		}

		return new IntegerLimit(limit);
	}

	private final int limit;

	private IntegerLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isWithinBounds(Limit ask) { // TODO not a fan of the name on this -- a bit confusing which limit goes where
		if (ask == this) {
			return true;
		}

		if (ask instanceof IntegerLimit) {
			return isWithinBounds(((IntegerLimit) ask).limit);
		}

		if (ask == StandardLimits.UNLIMITED) {
			return false;
		}

		return ask == StandardLimits.NONE;
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

	public int intValue() {
		return limit;
	}

	@Override
	public Limit without(Limit other) {
		if (other instanceof IntegerLimit) {
			IntegerLimit that = (IntegerLimit) other;
			int value = limit - that.limit;
			if (value < 0) {
				return StandardLimits.NONE;
			}
			return IntegerLimit.of(value);
		}

		if (other == StandardLimits.NONE) {
			return this;
		}

		if (other == StandardLimits.UNLIMITED) {
			return StandardLimits.NONE;
		}

		throw new IllegalArgumentException("Could not subtract limit: " + other);
	}

}