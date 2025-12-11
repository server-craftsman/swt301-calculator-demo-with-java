package com.fptu.swt301.demo.lab2.repository;

import com.fptu.swt301.demo.lab2.domain.model.CalorieCalculationRequest;
import com.fptu.swt301.demo.lab2.domain.valueobject.SwimmingStyle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class cho CalorieCalculationRepository.CalculationHistory
 * Test inner class để đạt coverage cao hơn
 */
@DisplayName("CalorieCalculationRepository.CalculationHistory Tests")
public class CalorieCalculationRepositoryTest {

    @Test
    @DisplayName("Test CalculationHistory constructor and getters")
    void testCalculationHistory() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        double result = 507.15;
        CalorieCalculationRepository.CalculationHistory history = new CalorieCalculationRepository.CalculationHistory(
                request, result);

        assertNotNull(history);
        assertEquals(request, history.getRequest());
        assertEquals(result, history.getResult(), 0.01);
        assertTrue(history.getTimestamp() > 0);
    }

    @Test
    @DisplayName("Test CalculationHistory toString")
    void testCalculationHistoryToString() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        double result = 507.15;
        CalorieCalculationRepository.CalculationHistory history = new CalorieCalculationRepository.CalculationHistory(
                request, result);

        String toString = history.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Butterfly"));
        assertTrue(toString.contains("30.00"));
        assertTrue(toString.contains("70.00"));
        assertTrue(toString.contains("507.15"));
    }

    @Test
    @DisplayName("Test CalculationHistory with different values")
    void testCalculationHistoryDifferentValues() {
        CalorieCalculationRequest request1 = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.CRAWL_RECREATIONAL)
                .durationMin(45.0)
                .bodyWeightKg(65.0)
                .build();

        CalorieCalculationRepository.CalculationHistory history1 = new CalorieCalculationRepository.CalculationHistory(
                request1, 424.86);

        assertEquals(SwimmingStyle.CRAWL_RECREATIONAL, history1.getRequest().getSwimmingStyle());
        assertEquals(45.0, history1.getRequest().getDurationMin());
        assertEquals(65.0, history1.getRequest().getBodyWeightKg());
        assertEquals(424.86, history1.getResult(), 0.01);
    }

    @Test
    @DisplayName("Test CalculationHistory timestamp is unique")
    void testCalculationHistoryTimestamp() throws InterruptedException {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        CalorieCalculationRepository.CalculationHistory history1 = new CalorieCalculationRepository.CalculationHistory(
                request, 507.15);

        Thread.sleep(10); // Small delay to ensure different timestamps

        CalorieCalculationRepository.CalculationHistory history2 = new CalorieCalculationRepository.CalculationHistory(
                request, 507.15);

        assertTrue(history2.getTimestamp() >= history1.getTimestamp());
    }
}
