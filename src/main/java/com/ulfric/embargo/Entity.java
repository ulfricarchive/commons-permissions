package com.ulfric.embargo;

import com.ulfric.embargo.limit.Limit;

import java.util.UUID;

public interface Entity {

	String getName();

	UUID getUniqueId();

	Allowance testPermission(String node);

	void setPermission(String node, Allowance allowance);

	void clearPermission(String node);

	Limit getLimit(String node);

	void setLimit(String node, Limit limit);

	void clearLimit(String node);

	void addParent(Entity entity);

	void removeParent(Entity entity);

}