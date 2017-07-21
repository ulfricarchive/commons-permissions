package com.ulfric.embargo.entity;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.google.common.truth.Truth;

import com.ulfric.embargo.entity.User;

import java.util.UUID;

@RunWith(JUnitPlatform.class)
class UserTest {

	@Test
	void testGetUserByUniqueId() {
		UUID uniqueId = UUID.randomUUID();
		User user = new User(uniqueId.toString(), uniqueId);
		Truth.assertThat(User.getUser(uniqueId)).isSameAs(user);
	}

	@Test
	void testGetUserByName() {
		UUID uniqueId = UUID.randomUUID();
		User user = new User(uniqueId.toString(), uniqueId);
		Truth.assertThat(User.getUser(uniqueId.toString())).isSameAs(user);
	}

}