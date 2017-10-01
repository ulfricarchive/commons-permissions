package com.ulfric.commons.permissions.entity;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import com.ulfric.commons.collection.MapHelper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class User extends SkeletalEntity {

	private static final Map<String, User> USERS_BY_NAME = new CaseInsensitiveMap<>();
	private static final ConcurrentMap<UUID, User> USERS_BY_ID = MapHelper.newConcurrentMap(8);

	public static User getUser(UUID uniqueId) {
		return USERS_BY_ID.get(uniqueId);
	}

	public static User getUser(String name) {
		return USERS_BY_NAME.get(name);
	}

	public User(String name, UUID uniqueId) {
		super(name, uniqueId);

		USERS_BY_NAME.put(name, this);
		USERS_BY_ID.put(uniqueId, this);
	}

}