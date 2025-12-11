package com.fptu.swt301.demo.lab2.main;

import com.fptu.swt301.demo.lab2.service.SwimmingCalorieService;
import com.fptu.swt301.demo.lab2.domain.model.CalorieCalculationRequest;
import com.fptu.swt301.demo.lab2.exception.ValidationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Test class cho Swimming Calorie Calculator
 * Đọc test data từ CSV file và sử dụng phương pháp:
 * - Phân vùng tương đương (Equivalence Partitioning)
 * - Giá trị biên (Boundary Value Analysis)
 * - Combination Testing
 * - Extreme Cases
 * 
 * Test coverage mục tiêu:
 * - 100% Statement Coverage
 * - 100% Branch Coverage
 * - 100% Boundary Value Analysis Coverage
 * - 100% Equivalence Partitioning Coverage
 */
public class SwimmingCalorieCalculatorTest {

    private SwimmingCalorieService calorieService;
    private List<SwimmingTestData> testDataList;

    @BeforeEach
    void setUp() {
        calorieService = new SwimmingCalorieService();
        testDataList = readCsvFile("/lab2/lab2_test_data.csv");
        System.out.println("\n========================================");
        System.out.println("SWIMMING CALORIE CALCULATOR TEST");
        System.out.println("Total test cases: " + testDataList.size());
        System.out.println("========================================\n");
    }

    @TestFactory
    @DisplayName("Dynamic Tests for Swimming Calorie Calculation from CSV")
    Collection<DynamicTest> dynamicTestsFromCsv() {
        List<DynamicTest> dynamicTests = new ArrayList<>();
        for (int i = 0; i < testDataList.size(); i++) {
            SwimmingTestData data = testDataList.get(i);
            int testCaseNumber = i + 1;

            dynamicTests.add(dynamicTest(
                    String.format("TC #%d: %s - %s", testCaseNumber, data.testCaseId, data.testCaseDescription),
                    () -> {
                        System.out.printf("Test Case #%d: %s\n", testCaseNumber, data.testCaseId);
                        System.out.printf("  Description: %s\n", data.testCaseDescription);
                        System.out.printf("  Category: %s\n", data.testCategory);
                        if (data.notes != null && !data.notes.isEmpty()) {
                            System.out.printf("  Notes: %s\n", data.notes);
                        }
                        System.out.printf("  Input:\n");
                        System.out.printf("    - Swimming Style: %s\n", data.swimmingStyle);
                        System.out.printf("    - Duration: %.2f minutes\n", data.durationMin);
                        System.out.printf("    - Body Weight: %.2f kg\n", data.bodyWeightKg);

                        try {
                            if ("EXCEPTION".equalsIgnoreCase(data.expectedResultWeb)) {
                                // Expected to throw an exception
                                Exception exception = assertThrows(ValidationException.class, () -> {
                                    calorieService.calculateCaloriesBurned(
                                            data.swimmingStyle,
                                            data.durationMin,
                                            data.bodyWeightKg);
                                });
                                System.out.printf("  Expected: Exception (ValidationException)\n");
                                System.out.printf("  Actual: Exception thrown - %s\n", exception.getMessage());
                                if (exception instanceof ValidationException) {
                                    ValidationException validationException = (ValidationException) exception;
                                    if (validationException.getErrorCount() > 1) {
                                        System.out.printf("  Errors (%d):\n", validationException.getErrorCount());
                                        validationException.getErrors()
                                                .forEach(error -> System.out.printf("    - %s\n", error));
                                    }
                                }
                                System.out.printf("  Status: ✓ PASSED\n");
                            } else {
                                // Expected normal calculation
                                double expectedCalories = Double.parseDouble(data.expectedResultWeb);

                                // Lấy giá trị chính xác (không làm tròn) để hiển thị
                                CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                                        .swimmingStyle(data.swimmingStyle)
                                        .durationMin(data.durationMin)
                                        .bodyWeightKg(data.bodyWeightKg)
                                        .build();

                                // Lấy giá trị exact từ service
                                double actualCaloriesExact = calorieService.calculateCaloriesBurnedExact(request);
                                double actualCalories = calorieService.calculateCaloriesBurned(
                                        data.swimmingStyle,
                                        data.durationMin,
                                        data.bodyWeightKg);

                                // Tính calories per minute exact
                                double caloriesPerMinExact = calorieService.calculateCaloriesPerMinuteExact(request);
                                double caloriesPerMin = calorieService.calculateCaloriesPerMinute(request);

                                // Hiển thị với nhiều chữ số thập phân (không làm tròn)
                                System.out.printf("  Expected Calories: %.10f kcal (from CSV: %.2f)\n",
                                        expectedCalories, expectedCalories);
                                System.out.printf("  Actual Calories (exact):   %.10f kcal\n", actualCaloriesExact);
                                System.out.printf("  Actual Calories (rounded): %.2f kcal\n", actualCalories);
                                System.out.printf("  Calories per minute (exact):   %.10f kcal/min\n",
                                        caloriesPerMinExact);
                                System.out.printf("  Calories per minute (rounded): %.2f kcal/min\n", caloriesPerMin);

                                // So sánh với giá trị rounded (để khớp với test cases)
                                double difference = Math.abs(expectedCalories - actualCalories);
                                // Cho phép sai số nhỏ do làm tròn (0.01 kcal)
                                if (difference <= 0.02) {
                                    System.out.printf("  Status: ✓ PASSED (difference: %.10f kcal)\n", difference);
                                } else {
                                    System.out.printf("  Status: ✗ FAILED (difference: %.10f kcal)\n", difference);
                                    System.out.printf(
                                            "  Details: Expected %.2f kcal, but got %.2f kcal (difference: %.10f kcal)\n",
                                            expectedCalories, actualCalories, difference);
                                    fail(String.format(
                                            "Calories burned mismatch for test case %s: expected %.2f kcal, got %.2f kcal (difference: %.10f kcal)",
                                            data.testCaseId, expectedCalories, actualCalories, difference));
                                }
                            }
                        } catch (ValidationException e) {
                            if ("EXCEPTION".equalsIgnoreCase(data.expectedResultWeb)) {
                                System.out.printf("  Expected: ValidationException\n");
                                System.out.printf("  Actual: ValidationException with %d errors:\n", e.getErrorCount());
                                e.getErrors().forEach(error -> System.out.printf("    - %s\n", error));
                                System.out.printf("  Status: ✓ PASSED\n");
                            } else {
                                System.out.printf("  Status: ✗ FAILED (Unexpected ValidationException)\n");
                                System.out.printf("  Error: %s\n", e.getMessage());
                                if (e.getErrorCount() > 1) {
                                    e.getErrors().forEach(error -> System.out.printf("    - %s\n", error));
                                }
                                fail(String.format("Test case %s failed with unexpected ValidationException: %s",
                                        data.testCaseId, e.getMessage()));
                            }
                        } catch (Exception e) {
                            if ("EXCEPTION".equalsIgnoreCase(data.expectedResultWeb)) {
                                System.out.printf("  Expected: Exception\n");
                                System.out.printf("  Actual: Exception thrown - %s\n", e.getMessage());
                                System.out.printf("  Status: ✓ PASSED\n");
                            } else {
                                System.out.printf("  Status: ✗ FAILED (Unexpected Exception)\n");
                                System.out.printf("  Error: %s\n", e.getMessage());
                                fail(String.format("Test case %s failed with unexpected exception: %s",
                                        data.testCaseId, e.getMessage()));
                            }
                        }
                        System.out.println();
                    }));
        }
        return dynamicTests;
    }

    /**
     * Đọc file CSV và trả về danh sách SwimmingTestData
     */
    private List<SwimmingTestData> readCsvFile(String resourcePath) {
        List<SwimmingTestData> testDataList = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // Bỏ qua dòng header
            String header = reader.readLine();
            if (header == null) {
                throw new RuntimeException("CSV file is empty");
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Skip comment lines (lines starting with #)
                if (line.trim().startsWith("#")) {
                    continue;
                }

                String[] values = parseCsvLine(line);
                if (values.length != 8) {
                    throw new RuntimeException(
                            String.format("Invalid CSV format at line %d: expected 8 columns, got %d",
                                    lineNumber, values.length));
                }

                try {
                    SwimmingTestData data = new SwimmingTestData();
                    data.testCaseId = values[0].trim();
                    data.testCaseDescription = values[1].trim();
                    data.swimmingStyle = values[2].trim();
                    data.durationMin = Double.parseDouble(values[3].trim());
                    data.bodyWeightKg = Double.parseDouble(values[4].trim());
                    data.expectedResultWeb = values[5].trim();
                    data.testCategory = values[6].trim();
                    data.notes = values.length > 7 ? values[7].trim() : "";

                    testDataList.add(data);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(
                            String.format("Invalid number format at line %d: %s", lineNumber, line), e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + resourcePath, e);
        }

        return testDataList;
    }

    /**
     * Parse CSV line, handling commas inside quoted fields
     */
    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString());

        return values.toArray(new String[0]);
    }

    /**
     * Class để lưu trữ dữ liệu test từ CSV
     */
    private static class SwimmingTestData {
        String testCaseId;
        String testCaseDescription;
        String swimmingStyle;
        double durationMin;
        double bodyWeightKg;
        String expectedResultWeb;
        String testCategory;
        String notes;
    }
}
