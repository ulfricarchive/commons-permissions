package com.ulfric.commons.permissions.entity;

import org.apache.commons.collections4.trie.PatriciaTrie;

import com.ulfric.commons.collection.MapHelper;
import com.ulfric.commons.permissions.limit.Limit;
import com.ulfric.commons.permissions.limit.StandardLimits;
import com.ulfric.commons.permissions.node.Allowance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public abstract class SkeletalEntity implements Entity {

	private final ConcurrentMap<String, Allowance> permissionCache = MapHelper.newConcurrentMap(1);
	protected final PatriciaTrie<Allowance> permissions = new PatriciaTrie<>();

	private final ConcurrentMap<String, Limit> limitsCache = MapHelper.newConcurrentMap(1);
	protected final PatriciaTrie<Limit> limits = new PatriciaTrie<>();

	protected final Map<UUID, Entity> parents = new LinkedHashMap<>();

	private final String name;
	private final UUID uniqueId;

	public SkeletalEntity(String name, UUID uniqueId) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(uniqueId, "uniqueId");

		this.name = name;
		this.uniqueId = uniqueId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
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

		for (Entity parent : parents.values()) {
			allowance = parent.testPermission(node); // TODO what if another parent gives a more specific entry
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

		for (Entity parent : parents.values()) {
			limit = parent.getLimit(node); // TODO what if another parent gives a more specific entry
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
		return limit == null || limit == StandardLimits.NONE;
	}

	@Override
	public void addParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");

		parents.put(entity.getUniqueId(), entity);
		recalculate();
	}

	@Override
	public void removeParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");

		if (parents.remove(entity.getUniqueId(), entity)) {
			recalculate();
		}
	}

	@Override
	public boolean hasParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");

		return parents.containsKey(entity.getUniqueId());
	}

	@Override
	public List<Entity> getParents() {
		return Collections.unmodifiableList(new ArrayList<>(parents.values()));
	}

	@Override
	public void recalculate() { // TODO clear caches of children
		limitsCache.clear();
		permissionCache.clear();
	}

}