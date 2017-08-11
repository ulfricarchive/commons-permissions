package com.ulfric.embargo.entity;

import org.junit.jupiter.api.Test;

import com.google.common.truth.Truth;

import java.util.UUID;

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