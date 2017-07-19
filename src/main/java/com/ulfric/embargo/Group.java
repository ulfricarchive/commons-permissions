package com.ulfric.embargo;

import com.ulfric.commons.value.UniqueIdHelper;

public class Group extends SkeletalEntity {

	static {
		setupEntityType(Group.class);
	}

	public static Group getGroup(String name) {
		return (Group) SkeletalEntity.ENTITIES_BY_NAME.get(Group.class).get(name);
	}

	public Group(String name) {
		super(name, UniqueIdHelper.nameUniqueId(name));
	}

}