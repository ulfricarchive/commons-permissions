package com.ulfric.embargo.limit;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.google.common.truth.Truth;

import com.ulfric.veracity.suite.EnumTestSuite;

@RunWith(JUnitPlatform.class)
class StandardLimitsTest extends EnumTestSuite {

	StandardLimitsTest() {
		super(StandardLimits.class);
	}

	@Test
	void testNoneNothingWithinBounds() {
		Truth.assertThat(StandardLimits.NONE.isWithinBounds(0)).isFalse();
	}

	@Test
	void testNoneAndAnything() {
		Truth.assertThat(StandardLimits.NONE.and(IntegerLimit.of(3))).isEqualTo(IntegerLimit.of(3));
	}

	@Test
	void testUnlimitedEverythingWithinBounds() {
		Truth.assertThat(StandardLimits.UNLIMITED.isWithinBounds(Integer.MAX_VALUE)).isTrue();
	}

	@Test
	void testUnlimitedEverythingAndAnything() {
		Truth.assertThat(StandardLimits.UNLIMITED.and(IntegerLimit.of(3))).isSameAs(StandardLimits.UNLIMITED);
	}

}