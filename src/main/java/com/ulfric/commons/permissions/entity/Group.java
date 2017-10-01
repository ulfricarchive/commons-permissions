package com.ulfric.commons.permissions.entity;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import com.ulfric.commons.value.UniqueIdHelper;

import java.util.Map;

public class Group extends SkeletalEntity {

	private static final Map<String, Group> GROUPS = new CaseInsensitiveMap<>();

	public static Group getGroup(String name) {
		return GROUPS.get(name);
	}

	public Group(String name) {
		super(name, UniqueIdHelper.nameUniqueId(name));

		GROUPS.put(name, this);
	}

}