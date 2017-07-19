package com.ulfric.embargo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.google.common.truth.Truth;

import com.ulfric.embargo.limit.IntegerLimit;
import com.ulfric.embargo.limit.StandardLimits;

import java.util.UUID;

@RunWith(JUnitPlatform.class)
class SkeletalEntityTest {

	private Entity entity;

	@BeforeEach
	void setup() {
		entity = new Entity();
	}

	@Test
	void testLookupPermissionNoPermissions() {
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.UNKNOWN);
	}

	@Test
	void testLookupPermissionPermissionGranted() {
		entity.setPermission("hello", Allowance.ALLOWED);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.ALLOWED);
	}

	@Test
	void testLookupPermissionPermissionChildGranted() {
		entity.setPermission("hello-child", Allowance.ALLOWED);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.ALLOWED);
	}

	@Test
	void testLookupPermissionPermissionFromParent() {
		Entity parent = new Entity();
		entity.addParent(parent);
		parent.setPermission("hello", Allowance.ALLOWED);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.ALLOWED);
	}

	@Test
	void testRemovePermission() {
		entity.setPermission("hello", Allowance.ALLOWED);
		entity.setPermission("hello", null);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.UNKNOWN);
	}

	@Test
	void testPermissionExpectations() {
		entity.setPermission("hello-world-one-two", Allowance.ALLOWED);
		entity.setPermission("hello-world-one", Allowance.DENIED);
		entity.setPermission("hello-world", Allowance.ALLOWED);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.ALLOWED);
		Truth.assertThat(entity.testPermission("hello-world")).isSameAs(Allowance.ALLOWED);
		Truth.assertThat(entity.testPermission("hello-world-one")).isSameAs(Allowance.DENIED);
		Truth.assertThat(entity.testPermission("hello-world-one-two")).isSameAs(Allowance.ALLOWED);
	}

	@Test
	void testLookupPermissionPermissionFromParentWithoutNode() {
		Entity parent = new Entity();
		entity.addParent(parent);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.UNKNOWN);
	}

	@Test
	void testLookupLimitNoPermissions() {
		Truth.assertThat(entity.getLimit("hello")).isSameAs(StandardLimits.NONE);
	}

	@Test
	void testLookupLimitUnlimitedGranted() {
		entity.setLimit("hello", StandardLimits.UNLIMITED);
		Truth.assertThat(entity.getLimit("hello")).isSameAs(StandardLimits.UNLIMITED);
	}

	@Test
	void testLookupLimitUnlimitedChildGranted() {
		entity.setLimit("hello-child", StandardLimits.UNLIMITED);
		Truth.assertThat(entity.getLimit("hello")).isSameAs(StandardLimits.UNLIMITED);
	}

	@Test
	void testLookupLimitUnlimitedFromParent() {
		Entity parent = new Entity();
		entity.addParent(parent);
		parent.setLimit("hello", StandardLimits.UNLIMITED);
		Truth.assertThat(entity.getLimit("hello")).isSameAs(StandardLimits.UNLIMITED);
	}

	@Test
	void testRemoveLimit() {
		entity.setLimit("hello", StandardLimits.UNLIMITED);
		entity.setLimit("hello", null);
		Truth.assertThat(entity.getLimit("hello")).isSameAs(StandardLimits.NONE);
	}

	@Test
	void testLimitExpectations() {
		entity.setLimit("hello-world-one-two", StandardLimits.UNLIMITED);
		entity.setLimit("hello-world-one", IntegerLimit.of(5));
		entity.setLimit("hello-world", StandardLimits.UNLIMITED);
		Truth.assertThat(entity.getLimit("hello")).isSameAs(StandardLimits.UNLIMITED);
		Truth.assertThat(entity.getLimit("hello-world")).isSameAs(StandardLimits.UNLIMITED);
		Truth.assertThat(entity.getLimit("hello-world-one")).isEqualTo(IntegerLimit.of(5));
		Truth.assertThat(entity.getLimit("hello-world-one-two")).isSameAs(StandardLimits.UNLIMITED);
	}

	@Test
	void testLookupLimitPermissionFromParentWithoutNode() {
		Entity parent = new Entity();
		entity.addParent(parent);
		Truth.assertThat(entity.getLimit("hello")).isSameAs(StandardLimits.NONE);
	}

	@Test
	void testRemoveParent() {
		Entity parent = new Entity();
		entity.addParent(parent);
		parent.setPermission("hello", Allowance.ALLOWED);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.ALLOWED);
		entity.removeParent(parent);
		Truth.assertThat(entity.testPermission("hello")).isSameAs(Allowance.UNKNOWN);
	}

	@Test
	void testCoverage() { // TODO extract testable
		entity.removeParent(entity);

		entity.setPermission("hello", Allowance.ALLOWED);
		entity.setPermission("hello", Allowance.ALLOWED);
		entity.setPermission("hello", null);
		entity.setPermission("hello", null);
		entity.setPermission("hello", Allowance.UNKNOWN);

		entity.setLimit("hello", StandardLimits.UNLIMITED);
		entity.setLimit("hello", StandardLimits.UNLIMITED);
		entity.setLimit("hello", null);
		entity.setLimit("hello", null);
		entity.setLimit("hello", StandardLimits.NONE);

		entity.getName();
	}

	static class Entity extends Group {
		public Entity() {
			super(UUID.randomUUID().toString());
		}
	}

}