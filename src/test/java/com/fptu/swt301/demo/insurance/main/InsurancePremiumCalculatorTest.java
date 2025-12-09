package com.fptu.swt301.demo.insurance.main;

import com.fptu.swt301.demo.insurance.service.PremiumCalculationService;
import com.fptu.swt301.demo.insurance.exception.ValidationException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class cho InsurancePremiumCalculator
 * Đọc test data từ CSV file và sử dụng phương pháp:
 * - Phân vùng tương đương (Equivalence Partitioning)
 * - Giá trị biên (Boundary Value Analysis)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InsurancePremiumCalculatorTest {

    private final PremiumCalculationService premiumService = new PremiumCalculationService();

    /**
     * Test tất cả các test cases từ CSV file
     * File CSV chứa 25 test cases bao gồm:
     * - Boundary Value Analysis: giá trị biên cho EstimatedValue, TotalMileage,
     * NumberOfAccidents
     * - Equivalence Partitioning: các phân vùng khác nhau của breakdown cover,
     * windscreen repair, parking location
     * - Combination Testing: kết hợp nhiều yếu tố
     * - Extreme Cases: các trường hợp cực biên
     */
    @Test
    @DisplayName("Test Insurance Premium from CSV File")
    @Order(1)
    public void testInsurancePremiumFromCsvFile() {
        List<InsuranceTestData> testDataList = readCsvFile("/insurance_premium_test_data.csv");

        System.out.println("\n========================================");
        System.out.println("INSURANCE PREMIUM CALCULATOR TEST");
        System.out.println("Total test cases: " + testDataList.size());
        System.out.println("========================================\n");

        int testCaseNumber = 1;
        int passedCount = 0;
        int failedCount = 0;

        for (InsuranceTestData data : testDataList) {
            try {
                System.out.printf("Test Case #%d: %s\n", testCaseNumber, data.testCaseId);
                System.out.printf("  Description: %s\n", data.testCaseDescription);
                System.out.printf("  Category: %s\n", data.testCategory);
                System.out.printf("  Input:\n");
                System.out.printf("    - Breakdown Cover: %s\n", data.breakdownCover);
                System.out.printf("    - Windscreen Repair: %s\n", data.windscreenRepair);
                System.out.printf("    - Number of Accidents: %d\n", data.numberOfAccidents);
                System.out.printf("    - Total Mileage: %d\n", data.totalMileage);
                System.out.printf("    - Estimated Value: £%.2f\n", data.estimatedValue);
                System.out.printf("    - Parking Location: %s\n", data.parkingLocation);

                if ("EXCEPTION".equalsIgnoreCase(data.expectedPremium)) {
                    // Test case mong đợi exception
                    System.out.printf("  Expected: Exception (IllegalArgumentException)\n");

                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        premiumService.calculatePremium(
                                data.breakdownCover,
                                data.windscreenRepair,
                                data.numberOfAccidents,
                                data.totalMileage,
                                data.estimatedValue,
                                data.parkingLocation);
                    });

                    if (exception instanceof ValidationException) {
                        ValidationException ve = (ValidationException) exception;
                        if (ve.getErrorCount() > 1) {
                            System.out.printf("  Actual: ValidationException with %d errors:\n", ve.getErrorCount());
                            for (int i = 0; i < ve.getErrors().size(); i++) {
                                System.out.printf("    %d. %s\n", i + 1, ve.getErrors().get(i));
                            }
                        } else {
                            System.out.printf("  Actual: Exception thrown - %s\n", exception.getMessage());
                        }
                    } else {
                        System.out.printf("  Actual: Exception thrown - %s\n", exception.getMessage());
                    }
                    System.out.printf("  Status: ✓ PASSED\n");
                    passedCount++;

                } else {
                    // Test case bình thường
                    double expectedPremium = Double.parseDouble(data.expectedPremium);
                    double actualPremium = premiumService.calculatePremium(
                            data.breakdownCover,
                            data.windscreenRepair,
                            data.numberOfAccidents,
                            data.totalMileage,
                            data.estimatedValue,
                            data.parkingLocation);

                    System.out.printf("  Expected Premium: £%.2f\n", expectedPremium);
                    System.out.printf("  Actual Premium:   £%.2f\n", actualPremium);

                    double difference = Math.abs(expectedPremium - actualPremium);
                    // Cho phép sai số 0.02 để xử lý floating point precision và làm tròn
                    boolean passed = difference <= 0.02;

                    if (passed) {
                        System.out.printf("  Status: ✓ PASSED (difference: £%.2f)\n", difference);
                        passedCount++;
                    } else {
                        System.out.printf("  Status: ✗ FAILED (difference: £%.2f)\n", difference);
                        failedCount++;
                    }

                    // Sử dụng delta = 0.02 để chấp nhận sai số làm tròn nhỏ
                    assertEquals(expectedPremium, actualPremium, 0.02,
                            String.format(
                                    "Premium mismatch for test case %s: expected £%.2f, got £%.2f (difference: £%.2f)",
                                    data.testCaseId, expectedPremium, actualPremium, difference));
                }

            } catch (ValidationException e) {
                if (!"EXCEPTION".equalsIgnoreCase(data.expectedPremium)) {
                    failedCount++;
                    System.out.printf("  Status: ✗ FAILED (Unexpected ValidationException)\n");
                    if (e.getErrorCount() > 1) {
                        System.out.printf("  Found %d validation errors:\n", e.getErrorCount());
                        for (int i = 0; i < e.getErrors().size(); i++) {
                            System.out.printf("    %d. %s\n", i + 1, e.getErrors().get(i));
                        }
                    } else {
                        System.out.printf("  Error: %s\n", e.getMessage());
                    }
                    fail(String.format("Test case %s failed with unexpected ValidationException: %s",
                            data.testCaseId, e.getMessage()));
                } else {
                    // Expected exception
                    if (e.getErrorCount() > 1) {
                        System.out.printf("  Actual: ValidationException with %d errors:\n", e.getErrorCount());
                        for (int i = 0; i < e.getErrors().size(); i++) {
                            System.out.printf("    %d. %s\n", i + 1, e.getErrors().get(i));
                        }
                    } else {
                        System.out.printf("  Actual: Exception thrown - %s\n", e.getMessage());
                    }
                    System.out.printf("  Status: ✓ PASSED\n");
                    passedCount++;
                }
            } catch (Exception e) {
                if (!"EXCEPTION".equalsIgnoreCase(data.expectedPremium)) {
                    failedCount++;
                    System.out.printf("  Status: ✗ FAILED (Unexpected Exception)\n");
                    System.out.printf("  Error: %s\n", e.getMessage());
                    fail(String.format("Test case %s failed with unexpected exception: %s",
                            data.testCaseId, e.getMessage()));
                }
            }

            System.out.println();
            testCaseNumber++;
        }

        // In tổng kết
        System.out.println("========================================");
        System.out.println("RESULT SUMMARY:");
        System.out.println("========================================");
        System.out.printf("Total test cases: %d\n", testDataList.size());
        System.out.printf("Passed: %d\n", passedCount);
        System.out.printf("Failed: %d\n", failedCount);
        System.out.println("========================================");

        // Assert tổng kết
        if (failedCount > 0) {
            System.out.printf("\nTEST FAILED: %d out of %d test cases failed!\n", failedCount, testDataList.size());
            assertEquals(0, failedCount,
                    String.format("Expected all tests to pass, but %d test case(s) failed", failedCount));
        } else {
            System.out.printf("\nALL TESTS PASSED: %d/%d test cases passed!\n", passedCount, testDataList.size());
        }
        System.out.println();
    }

    /**
     * Đọc file CSV và trả về danh sách InsuranceTestData
     */
    private List<InsuranceTestData> readCsvFile(String resourcePath) {
        List<InsuranceTestData> testDataList = new ArrayList<>();

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

                String[] values = line.split(",");
                if (values.length != 10) {
                    throw new RuntimeException(
                            String.format("Invalid CSV format at line %d: expected 10 columns, got %d",
                                    lineNumber, values.length));
                }

                try {
                    InsuranceTestData data = new InsuranceTestData();
                    data.testCaseId = values[0].trim();
                    data.testCaseDescription = values[1].trim();
                    data.breakdownCover = values[2].trim();
                    data.windscreenRepair = values[3].trim();
                    data.numberOfAccidents = Integer.parseInt(values[4].trim());
                    data.totalMileage = Integer.parseInt(values[5].trim());
                    data.estimatedValue = Double.parseDouble(values[6].trim());
                    data.parkingLocation = values[7].trim();
                    data.expectedPremium = values[8].trim();
                    data.testCategory = values[9].trim();

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
     * Class để lưu trữ dữ liệu test từ CSV
     */
    private static class InsuranceTestData {
        String testCaseId;
        String testCaseDescription;
        String breakdownCover;
        String windscreenRepair;
        int numberOfAccidents;
        int totalMileage;
        double estimatedValue;
        String parkingLocation;
        String expectedPremium;
        String testCategory;
    }

    // ==================== Individual Unit Tests ====================

    /**
     * Test riêng cho breakdown cover - No cover (1%)
     */
    @Test
    @Disabled
    @Order(2)
    public void testBreakdownCover_NoCover() {
        double premium = premiumService.calculatePremium("No cover", "No", 1, 3000, 1000, "Driveway/Carport");
        assertEquals(1010.0, premium, 0.01, "No cover should increase by 1%");
    }

    /**
     * Test riêng cho breakdown cover - Roadside (2%)
     */
    @Test
    @Disabled
    @Order(3)
    public void testBreakdownCover_Roadside() {
        double premium = premiumService.calculatePremium("Roadside", "No", 1, 3000, 1000, "Driveway/Carport");
        assertEquals(1020.0, premium, 0.01, "Roadside should increase by 2%");
    }

    /**
     * Test riêng cho breakdown cover - At home (3%)
     */
    @Test
    @Disabled
    @Order(4)
    public void testBreakdownCover_AtHome() {
        double premium = premiumService.calculatePremium("At home", "No", 1, 3000, 1000, "Driveway/Carport");
        assertEquals(1030.0, premium, 0.01, "At home should increase by 3%");
    }

    /**
     * Test riêng cho breakdown cover - European (4%)
     */
    @Test
    @Disabled
    @Order(5)
    public void testBreakdownCover_European() {
        double premium = premiumService.calculatePremium("European", "No", 1, 3000, 1000, "Driveway/Carport");
        assertEquals(1040.0, premium, 0.01, "European should increase by 4%");
    }

    /**
     * Test riêng cho windscreen repair - Yes (thêm £30)
     */
    @Test
    @Disabled
    @Order(6)
    public void testWindscreenRepair_Yes() {
        double premium = premiumService.calculatePremium("No cover", "Yes", 1, 3000, 1000, "Driveway/Carport");
        assertEquals(1040.0, premium, 0.01, "Windscreen repair should add £30");
    }

    /**
     * Test riêng cho zero accidents - discount 30%
     */
    @Test
    @Disabled
    @Order(7)
    public void testZeroAccidents_Discount() {
        double premium = premiumService.calculatePremium("No cover", "No", 0, 3000, 1000, "Driveway/Carport");
        assertEquals(707.0, premium, 0.01, "Zero accidents should give 30% discount");
    }

    /**
     * Test riêng cho high mileage - thêm £50
     */
    @Test
    @Disabled
    @Order(8)
    public void testHighMileage_Above5000() {
        double premium = premiumService.calculatePremium("No cover", "No", 1, 6000, 1000, "Driveway/Carport");
        assertEquals(1060.0, premium, 0.01, "Mileage >5000 should add £50");
    }

    /**
     * Test riêng cho public parking - thêm £30
     */
    @Test
    @Disabled
    @Order(9)
    public void testPublicParking() {
        double premium = premiumService.calculatePremium("No cover", "No", 1, 3000, 1000, "Public Place");
        assertEquals(1040.0, premium, 0.01, "Public parking should add £30");
    }

    /**
     * Test riêng cho estimated value < 100 - Exception
     */
    @Test
    @Disabled
    @Order(10)
    public void testEstimatedValue_LessThan100_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            premiumService.calculatePremium("No cover", "No", 0, 3000, 99, "Driveway/Carport");
        });
        assertTrue(exception.getMessage().contains("at least £100"));
    }

    /**
     * Test riêng cho estimated value = 100 (boundary value)
     */
    @Test
    @Disabled
    @Order(11)
    public void testEstimatedValue_Exactly100() {
        double premium = premiumService.calculatePremium("No cover", "No", 0, 3000, 100, "Driveway/Carport");
        assertEquals(707.0, premium, 0.01, "Estimated value of exactly £100 should be valid");
    }
}
