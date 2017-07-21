package com.ulfric.embargo;

import com.ulfric.commons.service.Service;
import com.ulfric.embargo.entity.Group;
import com.ulfric.embargo.entity.User;

import java.util.List;
import java.util.UUID;

public interface PermissionsService extends Service {

	List<User> getActiveUsers();

	User getUserByName(String name);

	User getUserByUniqueId(UUID uniqueId);

	User createUser(UUID uniqueId, String name);

	Group getGroupByName(String name);

	Group createGroupByName(String name);

	List<Group> getActiveGroups();

}