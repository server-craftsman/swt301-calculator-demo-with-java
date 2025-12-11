package com.fptu.swt301.demo.lab2.service;

import com.fptu.swt301.demo.lab2.domain.model.CalorieCalculationRequest;
import com.fptu.swt301.demo.lab2.domain.valueobject.SwimmingStyle;
import com.fptu.swt301.demo.lab2.repository.CalorieCalculationRepository;
import com.fptu.swt301.demo.lab2.repository.InMemoryCalorieCalculationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class cho SwimmingCalorieService
 * Test tất cả các methods để đạt coverage cao hơn
 */
@DisplayName("SwimmingCalorieService Tests")
public class SwimmingCalorieServiceTest {

    private SwimmingCalorieService service;

    @BeforeEach
    void setUp() {
        service = new SwimmingCalorieService();
    }

    @Test
    @DisplayName("Test constructor with null repository - should throw exception")
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SwimmingCalorieService(null);
        });
    }

    @Test
    @DisplayName("Test constructor with custom repository")
    void testConstructorWithCustomRepository() {
        CalorieCalculationRepository mockRepository = new InMemoryCalorieCalculationRepository();
        SwimmingCalorieService customService = new SwimmingCalorieService(mockRepository);
        assertNotNull(customService);
    }

    @Test
    @DisplayName("Test calculateCaloriesBurnedFormatted - standard format")
    void testCalculateCaloriesBurnedFormattedStandard() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        String result = service.calculateCaloriesBurnedFormatted(request, false);
        assertEquals("507.15 kcal", result);
    }

    @Test
    @DisplayName("Test calculateCaloriesBurnedFormatted - European format")
    void testCalculateCaloriesBurnedFormattedEuropean() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        String result = service.calculateCaloriesBurnedFormatted(request, true);
        assertEquals("507,15 kcal", result); // No trailing zeros
    }

    @Test
    @DisplayName("Test calculateCaloriesPerMinuteFormatted - standard format")
    void testCalculateCaloriesPerMinuteFormattedStandard() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        String result = service.calculateCaloriesPerMinuteFormatted(request, false);
        assertEquals("16.91 kcal/min", result);
    }

    @Test
    @DisplayName("Test calculateCaloriesPerMinuteFormatted - European format")
    void testCalculateCaloriesPerMinuteFormattedEuropean() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        String result = service.calculateCaloriesPerMinuteFormatted(request, true);
        assertEquals("16,91 kcal/min", result);
    }

    @Test
    @DisplayName("Test calculateCaloriesBurnedDetailedFormatted - standard format")
    void testCalculateCaloriesBurnedDetailedFormattedStandard() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        String result = service.calculateCaloriesBurnedDetailedFormatted(request, false);
        assertEquals("507.15 kcal", result); // No trailing zeros
    }

    @Test
    @DisplayName("Test calculateCaloriesBurnedDetailedFormatted - European format")
    void testCalculateCaloriesBurnedDetailedFormattedEuropean() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        String result = service.calculateCaloriesBurnedDetailedFormatted(request, true);
        assertEquals("507,15 kcal", result); // No trailing zeros
    }

    @Test
    @DisplayName("Test getCalculationHistory")
    void testGetCalculationHistory() {
        // Clear history first
        service.clearHistory();

        // Perform some calculations
        CalorieCalculationRequest request1 = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();
        service.calculateCaloriesBurned(request1);

        CalorieCalculationRequest request2 = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.CRAWL_RECREATIONAL)
                .durationMin(45.0)
                .bodyWeightKg(65.0)
                .build();
        service.calculateCaloriesBurned(request2);

        List<CalorieCalculationRepository.CalculationHistory> history = service.getCalculationHistory();
        assertEquals(2, history.size());
        assertEquals(507.15, history.get(0).getResult(), 0.01);
        assertEquals(424.86, history.get(1).getResult(), 0.01);
    }

    @Test
    @DisplayName("Test clearHistory")
    void testClearHistory() {
        // Add some calculations
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();
        service.calculateCaloriesBurned(request);

        assertEquals(1, service.getCalculationCount());

        // Clear history
        service.clearHistory();

        assertEquals(0, service.getCalculationCount());
        assertTrue(service.getCalculationHistory().isEmpty());
    }

    @Test
    @DisplayName("Test getCalculationCount")
    void testGetCalculationCount() {
        service.clearHistory();
        assertEquals(0, service.getCalculationCount());

        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        service.calculateCaloriesBurned(request);
        assertEquals(1, service.getCalculationCount());

        service.calculateCaloriesBurned(request);
        assertEquals(2, service.getCalculationCount());
    }

    @Test
    @DisplayName("Test calculateCaloriesBurnedExact")
    void testCalculateCaloriesBurnedExact() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        double exact = service.calculateCaloriesBurnedExact(request);
        double rounded = service.calculateCaloriesBurned(request);

        // Exact should be more precise
        assertEquals(507.15, exact, 0.0001);
        assertEquals(507.15, rounded, 0.01);
    }

    @Test
    @DisplayName("Test calculateCaloriesPerMinuteExact")
    void testCalculateCaloriesPerMinuteExact() {
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(SwimmingStyle.BUTTERFLY)
                .durationMin(30.0)
                .bodyWeightKg(70.0)
                .build();

        double exact = service.calculateCaloriesPerMinuteExact(request);
        double rounded = service.calculateCaloriesPerMinute(request);

        // Exact should be more precise
        assertEquals(16.905, exact, 0.0001);
        assertEquals(16.91, rounded, 0.01);
    }
}
