package com.ulfric.commons.permissions.entity;

import java.util.List;
import java.util.Map;

import com.ulfric.commons.permissions.Permissible;
import com.ulfric.commons.permissions.limit.Limit;
import com.ulfric.commons.permissions.node.Allowance;

public interface Entity extends Permissible {

	String getIdentifier();

	void setPermission(String node, Allowance allowance);

	void clearPermission(String node);

	Map<String, Allowance> getPermissions();

	void setLimit(String node, Limit limit);

	void clearLimit(String node);

	Map<String, Limit> getLimits();

	void addParent(Entity entity);

	void removeParent(Entity entity);

	boolean hasParent(Entity entity);

	List<Entity> getParents();

	void recalculate();

}