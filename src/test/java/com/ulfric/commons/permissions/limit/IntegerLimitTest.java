package com.ulfric.commons.permissions.limit;

import org.junit.jupiter.api.Test;

import com.google.common.truth.Truth;

import com.ulfric.commons.permissions.limit.IntegerLimit;
import com.ulfric.commons.permissions.limit.Limit;
import com.ulfric.commons.permissions.limit.StandardLimits;
import com.ulfric.veracity.Veracity;

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
	void testIsWithinBoundsIntegerLimit() {
		Truth.assertThat(IntegerLimit.of(5).isWithinBounds(IntegerLimit.of(4))).isTrue();
	}

	@Test
	void testIsWithinBoundsIntegerLimitSelf() {
		Limit self = IntegerLimit.of(1);
		Truth.assertThat(self.isWithinBounds(self)).isTrue();
	}

	@Test
	void testIsWithinBoundsIntegerLimitNull() {
		Truth.assertThat(IntegerLimit.of(1).isWithinBounds(null)).isFalse();
	}

	@Test
	void testIsWithinBoundsIntegerLimitNotWithinBounds() {
		Truth.assertThat(IntegerLimit.of(3).isWithinBounds(IntegerLimit.of(4))).isFalse();
	}

	@Test
	void testIsWithinBoundsUnlimited() {
		Truth.assertThat(IntegerLimit.of(3).isWithinBounds(StandardLimits.UNLIMITED)).isFalse();
	}

	@Test
	void testIsWithinBoundsNone() {
		Truth.assertThat(IntegerLimit.of(3).isWithinBounds(StandardLimits.NONE)).isTrue();
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

	@Test
	void testWithoutGoesNegative() {
		Truth.assertThat(IntegerLimit.of(3).without(IntegerLimit.of(5))).isSameAs(StandardLimits.NONE);
	}

	@Test
	void testWithoutUnlimited() {
		Truth.assertThat(IntegerLimit.of(3).without(StandardLimits.UNLIMITED)).isSameAs(StandardLimits.NONE);
	}

	@Test
	void testWithoutNone() {
		Truth.assertThat(IntegerLimit.of(3).without(StandardLimits.NONE)).isEqualTo(IntegerLimit.of(3));
	}

	@Test
	void testWithoutIntegerLimit() {
		Truth.assertThat(IntegerLimit.of(3).without(IntegerLimit.of(1))).isEqualTo(IntegerLimit.of(2));
	}

	@Test
	void testWithoutUnknown() {
		Veracity.assertThat(() -> IntegerLimit.of(3).without(null)).doesThrow(IllegalArgumentException.class);
	}

	@Test
	void testIntValue() {
		Truth.assertThat(IntegerLimit.of(3).intValue()).isEqualTo(3);
	}

}