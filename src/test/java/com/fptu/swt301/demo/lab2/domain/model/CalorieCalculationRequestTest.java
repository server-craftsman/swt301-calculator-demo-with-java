package com.fptu.swt301.demo.lab2.domain.model;

import com.fptu.swt301.demo.lab2.domain.valueobject.SwimmingStyle;
import com.fptu.swt301.demo.lab2.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class cho CalorieCalculationRequest
 * Test Builder và validation để đạt coverage cao hơn
 */
@DisplayName("CalorieCalculationRequest Tests")
public class CalorieCalculationRequestTest {

    @Test
    @DisplayName("Test Builder with SwimmingStyle enum")
    void testBuilderWithSwimmingStyleEnum() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        assertEquals(SwimmingStyle.BUTTERFLY, request.getSwimmingStyle());
        assertEquals(30.0, request.getDurationMin());
        assertEquals(70.0, request.getBodyWeightKg());
    }

    @Test
    @DisplayName("Test Builder with SwimmingStyle string - valid")
    void testBuilderWithSwimmingStyleStringValid() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle("Butterfly")
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        assertEquals(SwimmingStyle.BUTTERFLY, request.getSwimmingStyle());
    }

    @Test
    @DisplayName("Test Builder with SwimmingStyle string - invalid (should throw)")
    void testBuilderWithSwimmingStyleStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            CalorieCalculationRequest.builder()
                    .swimmingStyle("Invalid Style")
                    .durationMin(30.0)
                    .bodyWeightKg(70.0)
                    .build();
        });
    }

    @Test
    @DisplayName("Test Builder with null swimming style")
    void testBuilderWithNullSwimmingStyle() {
        assertThrows(ValidationException.class, () -> {
            CalorieCalculationRequest.builder()
                    .swimmingStyle((SwimmingStyle) null)
                    .durationMin(30.0)
                    .bodyWeightKg(70.0)
                    .build();
        });
    }

    @Test
    @DisplayName("Test Builder chaining")
    void testBuilderChaining() {
        CalorieCalculationRequest.Builder builder = CalorieCalculationRequest.builder();
        builder.swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0);

        CalorieCalculationRequest request = builder.build();
        assertNotNull(request);
        assertEquals(SwimmingStyle.BUTTERFLY, request.getSwimmingStyle());
    }

    @Test
    @DisplayName("Test equals and hashCode - uses Object default (reference equality)")
    void testEqualsAndHashCode() {
        CalorieCalculationRequest request1 = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        CalorieCalculationRequest request2 = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        // Object.equals() uses reference equality, so different instances are not equal
        assertNotEquals(request1, request2);
        assertNotEquals(request1, null);
        assertEquals(request1, request1); // Same reference

        // Test that getters return correct values
        assertEquals(SwimmingStyle.BUTTERFLY, request1.getSwimmingStyle());
        assertEquals(30.0, request1.getDurationMin());
        assertEquals(70.0, request1.getBodyWeightKg());
        assertEquals(SwimmingStyle.BUTTERFLY, request2.getSwimmingStyle());
        assertEquals(30.0, request2.getDurationMin());
        assertEquals(70.0, request2.getBodyWeightKg());
    }

    @Test
    @DisplayName("Test toString - uses Object default")
    void testToString() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        String toString = request.toString();
        assertNotNull(toString);
        // Object.toString() returns className@hashCode, so just verify it's not empty
        assertFalse(toString.isEmpty());
        // Verify it contains the class name
        assertTrue(toString.contains("CalorieCalculationRequest"));
    }

    @Test
    @DisplayName("Test build with null swimming style throws ValidationException")
    void testBuildWithNullSwimmingStyle() {
        assertThrows(ValidationException.class, () -> {
            CalorieCalculationRequest.builder()
                    .swimmingStyle((SwimmingStyle) null)
                    .durationMin(30.0)
                    .bodyWeightKg(70.0)
                    .build();
        });
    }

    @Test
    @DisplayName("Test build with multiple validation errors")
    void testBuildWithMultipleErrors() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            CalorieCalculationRequest.builder()
                    .swimmingStyle(SwimmingStyle.BUTTERFLY)
                    .durationMin(-10.0) // Negative duration
                    .bodyWeightKg(-5.0) // Negative weight
                    .build();
        });

        assertTrue(exception.getErrorCount() > 1);
    }
}
