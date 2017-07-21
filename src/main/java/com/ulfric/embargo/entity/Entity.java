package com.ulfric.embargo.entity;

import com.ulfric.embargo.Permissible;
import com.ulfric.embargo.limit.Limit;
import com.ulfric.embargo.node.Allowance;

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