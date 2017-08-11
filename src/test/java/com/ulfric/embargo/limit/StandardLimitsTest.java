package com.ulfric.embargo.limit;

import org.junit.jupiter.api.Test;

import com.google.common.truth.Truth;

import com.ulfric.veracity.suite.EnumTestSuite;

class StandardLimitsTest extends EnumTestSuite {

	StandardLimitsTest() {
		super(StandardLimits.class);
	}

	@Test
	void testNoneNothingWithinBounds() {
		Truth.assertThat(StandardLimits.NONE.isWithinBounds(0)).isFalse();
	}

	@Test
	void testNoneSelfWithinBounds() {
		Truth.assertThat(StandardLimits.NONE.isWithinBounds(StandardLimits.NONE)).isTrue();
	}

	@Test
	void testNoneOneLimitNotWithinBounds() {
		Truth.assertThat(StandardLimits.NONE.isWithinBounds(IntegerLimit.of(1))).isFalse();
	}

	@Test
	void testNoneAndAnything() {
		Truth.assertThat(StandardLimits.NONE.and(IntegerLimit.of(3))).isEqualTo(IntegerLimit.of(3));
	}

	@Test
	void testNoneWithoutAnything() {
		Truth.assertThat(StandardLimits.NONE.without(IntegerLimit.of(3))).isEqualTo(StandardLimits.NONE);
	}

	@Test
	void testUnlimitedEverythingWithinBounds() {
		Truth.assertThat(StandardLimits.UNLIMITED.isWithinBounds(Integer.MAX_VALUE)).isTrue();
	}

	@Test
	void testUnlimitedEverythingAndAnything() {
		Truth.assertThat(StandardLimits.UNLIMITED.and(IntegerLimit.of(3))).isSameAs(StandardLimits.UNLIMITED);
	}

	@Test
	void testUnlimitedEverythingWithoutAnything() {
		Truth.assertThat(StandardLimits.UNLIMITED.without(IntegerLimit.of(3))).isSameAs(StandardLimits.UNLIMITED);
	}

	@Test
	void testUnlimitedEverythingWithinBoundsLimit() {
		Truth.assertThat(StandardLimits.UNLIMITED.isWithinBounds(IntegerLimit.of(Integer.MAX_VALUE))).isTrue();
	}

}