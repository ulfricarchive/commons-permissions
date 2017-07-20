package com.ulfric.embargo;

import com.ulfric.embargo.limit.Limit;

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

	void recalculate();

}