package com.ulfric.embargo.limit;

public interface Limit {

	boolean isWithinBounds(int ask);

	Limit and(Limit other);

	Limit without(Limit other);

}