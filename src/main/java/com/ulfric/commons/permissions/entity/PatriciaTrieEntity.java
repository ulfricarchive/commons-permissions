package com.ulfric.commons.permissions.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections4.trie.PatriciaTrie;

import com.ulfric.commons.collection.ListHelper;
import com.ulfric.commons.collection.MapHelper;
import com.ulfric.commons.permissions.limit.Limit;
import com.ulfric.commons.permissions.limit.StandardLimits;
import com.ulfric.commons.permissions.node.Allowance;

public class PatriciaTrieEntity implements Entity {

	private final ConcurrentMap<String, Allowance> permissionCache = MapHelper.newConcurrentMap(1);
	protected final PatriciaTrie<Allowance> permissions = new PatriciaTrie<>();

	private final ConcurrentMap<String, Limit> limitsCache = MapHelper.newConcurrentMap(1);
	protected final PatriciaTrie<Limit> limits = new PatriciaTrie<>();

	protected final List<Entity> parents = new ArrayList<>();

	private final String identifier;

	public PatriciaTrieEntity(UUID identifier) {
		this(identifier.toString());
	}

	public PatriciaTrieEntity(String identifier) {
		Objects.requireNonNull(identifier, "identifier");
		this.identifier = identifier;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public Allowance testPermission(String node) {
		return permissionCache.computeIfAbsent(node,
				this::lookupPermission);
	}

	private Allowance lookupPermission(String node) {
		Allowance allowance = permissions.selectValue(node);
		if (allowance != null) {
			return allowance;
		}

		for (Entity parent : parents) {
			allowance = parent.testPermission(node);
			if (allowance != Allowance.UNDEFINED) {
				return allowance;
			}
		}

		return allowance == null ? Allowance.UNDEFINED : allowance;
	}

	@Override
	public Limit getLimit(String node) {
		return limitsCache.computeIfAbsent(node, this::lookupLimit);
	}

	private Limit lookupLimit(String node) {
		Limit limit = limits.selectValue(node);
		if (limit != null) {
			return limit;
		}

		for (Entity parent : parents) {
			limit = parent.getLimit(node);
			if (limit != StandardLimits.NONE) {
				return limit;
			}
		}

		return limit == null ? StandardLimits.NONE : limit;
	}

	@Override
	public void setPermission(String node, Allowance allowance) {
		Objects.requireNonNull(node, "node");

		if (isRemoval(allowance)) {
			clearPermission(node);
			return;
		}

		if (permissions.put(node, allowance) != allowance) {
			recalculate();
		}
	}

	@Override
	public void clearPermission(String node) {
		Objects.requireNonNull(node, "node");

		if (permissions.remove(node) != null) {
			recalculate();
		}
	}

	@Override
	public void setLimit(String node, Limit limit) {
		Objects.requireNonNull(node, "node");

		if (isRemoval(limit)) {
			clearLimit(node);
			return;
		}

		Limit old = limits.put(node, limit);
		if (!Objects.equals(old, limit)) {
			recalculate();
		}
	}

	@Override
	public void clearLimit(String node) {
		Objects.requireNonNull(node, "node");

		if (limits.remove(node) != null) {
			recalculate();
		}
	}

	private boolean isRemoval(Allowance allowance) {
		return allowance == null || allowance == Allowance.UNDEFINED;
	}

	private boolean isRemoval(Limit limit) {
		return limit == null;
	}

	@Override
	public void addParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");

		parents.add(entity);
		recalculate();
	}

	@Override
	public void removeParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");

		if (parents.remove(entity)) {
			recalculate();
		}
	}

	@Override
	public boolean hasParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");

		return parents.contains(entity);
	}

	@Override
	public List<Entity> getParents() {
		return ListHelper.unmodifiableCopy(parents);
	}

	@Override
	public Map<String, Allowance> getPermissions() {
		return MapHelper.unmodifiableCopy(permissions);
	}

	@Override
	public Map<String, Limit> getLimits() {
		return MapHelper.unmodifiableCopy(limits);
	}

	public void clear() {
		parents.clear();
		limits.clear();
		permissions.clear();
		recalculate();
	}

	@Override
	public void recalculate() { // TODO clear caches of children
		limitsCache.clear();
		permissionCache.clear();
	}

}
