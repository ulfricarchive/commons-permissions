package com.ulfric.commons.permissions;

import com.ulfric.commons.permissions.limit.Limit;
import com.ulfric.commons.permissions.node.Allowance;

public interface Permissible {

	Allowance testPermission(String node);

	Limit getLimit(String node);

}