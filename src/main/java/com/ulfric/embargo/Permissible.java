package com.ulfric.embargo;

import com.ulfric.embargo.limit.Limit;

public interface Permissible {

	Allowance testPermission(String node);

	Limit getLimit(String node);

}