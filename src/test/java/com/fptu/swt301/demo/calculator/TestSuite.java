package com.fptu.swt301.demo.calculator;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ TestDemo_Fixed.class, TestPasswordValidator.class })
public class TestSuite {
}
