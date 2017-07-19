package com.ulfric.embargo;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.ulfric.veracity.suite.EnumTestSuite;

@RunWith(JUnitPlatform.class)
class AllowanceTest extends EnumTestSuite {

	AllowanceTest() {
		super(Allowance.class);
	}

}