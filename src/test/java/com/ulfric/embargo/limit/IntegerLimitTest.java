package com.ulfric.embargo.limit;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.google.common.truth.Truth;

import com.ulfric.veracity.Veracity;

@RunWith(JUnitPlatform.class)
class IntegerLimitTest {

	@Test
	void testOfNegative() {
		Veracity.assertThat(() -> IntegerLimit.of(-1)).doesThrow(IllegalArgumentException.class);
	}

	@Test
	void testIsWithinBounds() {
		Truth.assertThat(IntegerLimit.of(5).isWithinBounds(4)).isTrue();
	}

	@Test
	void testIsWithinBoundsNotWithinBounds() {
		Truth.assertThat(IntegerLimit.of(3).isWithinBounds(4)).isFalse();
	}

	@Test
	void testAndUnlimited() {
		Truth.assertThat(IntegerLimit.of(3).and(StandardLimits.UNLIMITED)).isSameAs(StandardLimits.UNLIMITED);
	}

	@Test
	void testAndNone() {
		Truth.assertThat(IntegerLimit.of(3).and(StandardLimits.NONE)).isEqualTo(IntegerLimit.of(3));
	}

	@Test
	void testAndIntegerLimit() {
		Truth.assertThat(IntegerLimit.of(3).and(IntegerLimit.of(1))).isEqualTo(IntegerLimit.of(4));
	}

	@Test
	void testAndUnknown() {
		Veracity.assertThat(() -> IntegerLimit.of(3).and(null)).doesThrow(IllegalArgumentException.class);
	}

}