package com.ulfric.commons.permissions.entity;

import com.ulfric.commons.permissions.Permissible;
import com.ulfric.commons.permissions.limit.Limit;
import com.ulfric.commons.permissions.node.Allowance;

import java.util.List;
import java.util.UUID;

public interface Entity extends Permissible {

	String getName();

	UUID getUniqueId();

	void setPermission(String node, Allowance allowance);

	void clearPermission(String node);

	void setLimit(String node, Limit limit);

	void clearLimit(String node);

	void addParent(Entity entity);

	void removeParent(Entity entity);

	boolean hasParent(Entity entity);

	List<Entity> getParents();

	void recalculate();

}