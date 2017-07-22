package com.ulfric.embargo.entity;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.google.common.truth.Truth;

import java.util.UUID;

@RunWith(JUnitPlatform.class)
class GroupTest {

	@Test
	void testGetGroup() {
		UUID uniqueId = UUID.randomUUID();
		Group group = new Group(uniqueId.toString());
		Truth.assertThat(Group.getGroup(uniqueId.toString())).isSameAs(group);
	}

}