package com.ulfric.embargo;

import com.ulfric.embargo.entity.Group;
import com.ulfric.embargo.entity.User;

import java.util.UUID;

public interface PermissionsService {

	User getUserByName(String name);

	User getUserByUniqueId(UUID uniqueId);

	Group getGroupByName(String name);

}