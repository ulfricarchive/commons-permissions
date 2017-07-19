package com.ulfric.embargo;

import java.util.UUID;

public class User extends SkeletalEntity {

	static {
		setupEntityType(User.class);
	}

	public static User getUser(UUID uniqueId) {
		return (User) SkeletalEntity.ENTITIES_BY_ID.get(User.class).get(uniqueId);
	}

	public static User getUser(String name) {
		return (User) SkeletalEntity.ENTITIES_BY_NAME.get(User.class).get(name);
	}

	public User(String name, UUID uniqueId) {
		super(name, uniqueId);
	}

}