package com.ulfric.embargo.limit;

public interface Limit {

	boolean isWithinBounds(int ask);

	boolean isWithinBounds(Limit ask);

	Limit and(Limit other);

	Limit without(Limit other);

}