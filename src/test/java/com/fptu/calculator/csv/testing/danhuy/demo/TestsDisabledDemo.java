package com.fptu.calculator.csv.testing.danhuy.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestsDisabledDemo {
    @Disabled("Disabled until bug #42 has been resolved")
    @Test
    void testWillBeSkipped() {
    }

    @Test
    void testWillBeExecuted() {
    }
}
