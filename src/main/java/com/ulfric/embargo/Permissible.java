package com.ulfric.embargo;

import com.ulfric.embargo.limit.Limit;
import com.ulfric.embargo.node.Allowance;

public interface Permissible {

	Allowance testPermission(String node);

	Limit getLimit(String node);

}