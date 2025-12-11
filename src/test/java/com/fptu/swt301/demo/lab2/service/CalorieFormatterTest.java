package com.fptu.swt301.demo.lab2.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class cho CalorieFormatter
 * Test tất cả các formatting methods để đạt 100% coverage
 */
@DisplayName("CalorieFormatter Tests")
public class CalorieFormatterTest {

    @Test
    @DisplayName("Test formatCaloriesPerMinute - standard format")
    void testFormatCaloriesPerMinute() {
        assertEquals("16.91", CalorieFormatter.formatCaloriesPerMinute(16.905));
        assertEquals("0.02", CalorieFormatter.formatCaloriesPerMinute(0.02415));
        assertEquals("507.15", CalorieFormatter.formatCaloriesPerMinute(507.15));
        assertEquals("0.00", CalorieFormatter.formatCaloriesPerMinute(0.0));
        assertEquals("0.01", CalorieFormatter.formatCaloriesPerMinute(0.005));
    }

    @Test
    @DisplayName("Test formatTotalCalories - standard format")
    void testFormatTotalCalories() {
        assertEquals("507.15", CalorieFormatter.formatTotalCalories(507.15));
        assertEquals("0.72", CalorieFormatter.formatTotalCalories(0.7245));
        assertEquals("1449.00", CalorieFormatter.formatTotalCalories(1449.0));
        assertEquals("0.00", CalorieFormatter.formatTotalCalories(0.0));
    }

    @Test
    @DisplayName("Test formatTotalCaloriesDetailed - 5 decimal places")
    void testFormatTotalCaloriesDetailed() {
        assertEquals("0.16905", CalorieFormatter.formatTotalCaloriesDetailed(0.16905));
        assertEquals("507.15", CalorieFormatter.formatTotalCaloriesDetailed(507.15)); // No trailing zeros
        assertEquals("0.7245", CalorieFormatter.formatTotalCaloriesDetailed(0.7245)); // No trailing zeros
        assertEquals("0", CalorieFormatter.formatTotalCaloriesDetailed(0.0)); // No trailing zeros with # pattern
    }

    @Test
    @DisplayName("Test formatCaloriesPerMinuteEuropean - European format")
    void testFormatCaloriesPerMinuteEuropean() {
        assertEquals("16,91", CalorieFormatter.formatCaloriesPerMinuteEuropean(16.905));
        assertEquals("0,02", CalorieFormatter.formatCaloriesPerMinuteEuropean(0.02415));
        assertEquals("507,15", CalorieFormatter.formatCaloriesPerMinuteEuropean(507.15));
        assertEquals("0,00", CalorieFormatter.formatCaloriesPerMinuteEuropean(0.0));
    }

    @Test
    @DisplayName("Test formatTotalCaloriesEuropean - European format")
    void testFormatTotalCaloriesEuropean() {
        assertEquals("0,16905", CalorieFormatter.formatTotalCaloriesEuropean(0.16905));
        assertEquals("507,15", CalorieFormatter.formatTotalCaloriesEuropean(507.15)); // No trailing zeros
        assertEquals("0,7245", CalorieFormatter.formatTotalCaloriesEuropean(0.7245)); // No trailing zeros
        assertEquals("0", CalorieFormatter.formatTotalCaloriesEuropean(0.0)); // No trailing zeros with # pattern
    }

    @Test
    @DisplayName("Test formatWithUnit - standard format")
    void testFormatWithUnitStandard() {
        assertEquals("507.15 kcal", CalorieFormatter.formatWithUnit(507.15, false));
        assertEquals("0.72 kcal", CalorieFormatter.formatWithUnit(0.7245, false));
        assertEquals("0.00 kcal", CalorieFormatter.formatWithUnit(0.0, false));
    }

    @Test
    @DisplayName("Test formatWithUnit - European format")
    void testFormatWithUnitEuropean() {
        assertEquals("507,15 kcal", CalorieFormatter.formatWithUnit(507.15, true)); // No trailing zeros
        assertEquals("0,7245 kcal", CalorieFormatter.formatWithUnit(0.7245, true)); // No trailing zeros
        assertEquals("0 kcal", CalorieFormatter.formatWithUnit(0.0, true)); // No trailing zeros
    }

    @Test
    @DisplayName("Test edge cases - very small values")
    void testEdgeCasesSmallValues() {
        assertEquals("0.00", CalorieFormatter.formatCaloriesPerMinute(0.0001));
        assertEquals("0.00001", CalorieFormatter.formatTotalCaloriesDetailed(0.00001)); // Shows 5 decimal places
        assertEquals("0,00001", CalorieFormatter.formatTotalCaloriesEuropean(0.00001)); // Shows 5 decimal places
    }

    @Test
    @DisplayName("Test edge cases - large values")
    void testEdgeCasesLargeValues() {
        assertEquals("4600.57", CalorieFormatter.formatTotalCalories(4600.5749));
        assertEquals("4600.57499", CalorieFormatter.formatTotalCaloriesDetailed(4600.57499));
        assertEquals("4600,57499", CalorieFormatter.formatTotalCaloriesEuropean(4600.57499));
        // Test rounding
        assertEquals("4600.57", CalorieFormatter.formatTotalCalories(4600.575)); // Rounds down at 5
        assertEquals("4600.99999", CalorieFormatter.formatTotalCaloriesDetailed(4600.99999)); // Shows all 5 decimals
    }
}
