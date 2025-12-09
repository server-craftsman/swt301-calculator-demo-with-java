package com.fptu.calculator.csv.testing.danhuy.demo;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ TestDemo_Fixed.class, TestPasswordValidator.class })
public class TestSuite {
}
