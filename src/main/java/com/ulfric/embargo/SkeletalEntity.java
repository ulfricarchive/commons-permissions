package com.ulfric.embargo;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.trie.PatriciaTrie;

import com.ulfric.embargo.limit.Limit;
import com.ulfric.embargo.limit.StandardLimits;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class SkeletalEntity implements Entity {

	 // TODO limit cache size?
	static final Map<Class<?>, Map<String, SkeletalEntity>> ENTITIES_BY_NAME = new IdentityHashMap<>();
	static final Map<Class<?>, ConcurrentMap<UUID, SkeletalEntity>> ENTITIES_BY_ID = new IdentityHashMap<>();

	static void setupEntityType(Class<?> type) {
		SkeletalEntity.ENTITIES_BY_ID.putIfAbsent(type, new ConcurrentHashMap<>());
		SkeletalEntity.ENTITIES_BY_NAME.putIfAbsent(type, Collections.synchronizedMap(new CaseInsensitiveMap<>()));
	}

	private final Map<String, Allowance> permissionCache = new HashMap<>();
	private final PatriciaTrie<Allowance> permissions = new PatriciaTrie<>();

	private final Map<String, Limit> limitsCache = new HashMap<>();
	private final PatriciaTrie<Limit> limits = new PatriciaTrie<>();

	private final Map<UUID, Entity> parents = new LinkedHashMap<>();

	protected String name;
	protected UUID uniqueId;

	public SkeletalEntity(String name, UUID uniqueId) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(uniqueId, "uniqueId");

		this.name = name;
		this.uniqueId = uniqueId;
		registerThis();
	}

	private void registerThis() {
		Class<?> type = getClass();
		ENTITIES_BY_ID.computeIfAbsent(type, ignore -> new ConcurrentHashMap<>())
			.put(uniqueId, this);
		ENTITIES_BY_NAME.computeIfAbsent(type, ignore -> Collections.synchronizedMap(new CaseInsensitiveMap<>()))
			.put(name, this);
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
			if (allowance != Allowance.UNKNOWN) {
				return allowance;
			}
		}

		return allowance == null ? Allowance.UNKNOWN : allowance;
	}

	@Override
	public Limit getLimit(String node) {
		return limitsCache.computeIfAbsent(node.toLowerCase(), this::lookupLimit);
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
			clearCache();
		}
	}

	@Override
	public void clearPermission(String node) {
		Objects.requireNonNull(node, "node");

		if (permissions.remove(node) != null) {
			clearCache();
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
			clearCache();
		}
	}

	@Override
	public void clearLimit(String node) {
		Objects.requireNonNull(node, "node");

		if (limits.remove(node) != null) {
			clearCache();
		}
	}

	private boolean isRemoval(Allowance allowance) {
		return allowance == null || allowance == Allowance.UNKNOWN;
	}

	private boolean isRemoval(Limit limit) {
		return limit == null || limit == StandardLimits.NONE;
	}

	@Override
	public void addParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");
		parents.put(entity.getUniqueId(), entity);
		clearCache();
	}

	@Override
	public void removeParent(Entity entity) {
		Objects.requireNonNull(entity, "entity");
		if (parents.remove(entity.getUniqueId(), entity)) {
			clearCache();
		}
	}

	private void clearCache() { // TODO clear caches of children
		limitsCache.clear();
		permissionCache.clear();
	}

}
