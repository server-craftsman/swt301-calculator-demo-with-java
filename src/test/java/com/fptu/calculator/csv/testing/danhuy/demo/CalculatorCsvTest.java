package com.fptu.calculator.csv.testing.danhuy.demo;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorCsvTest {

    private final Calculator calculator = new Calculator();

    /**
     * Đọc dữ liệu từ file CSV và test tất cả các test cases
     *
     */
    @Test
    public void testCalculatorFromCsvFile() {
        List<TestData> testDataList = readCsvFile("/calculator_test_data.csv");

        System.out.println("\n========================================");
        System.out.println("Start testing calculator from CSV file");
        System.out.println("Total test cases: " + testDataList.size());
        System.out.println("========================================\n");

        int testCaseNumber = 1;
        int passedCount = 0;
        int failedCount = 0;

        for (TestData data : testDataList) {
            try {
                double actualResult;
                String operationSymbol = getOperationSymbol(data.operation);

                switch (data.operation.toLowerCase()) {
                    case "add":
                        actualResult = calculator.add(data.a, data.b);
                        break;
                    case "subtract":
                        actualResult = calculator.subtract(data.a, data.b);
                        break;
                    case "multiply":
                        actualResult = calculator.multiply(data.a, data.b);
                        break;
                    case "divide":
                        actualResult = calculator.divide(data.a, data.b);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operation: " + data.operation);
                }

                // Kiểm tra kết quả
                double difference = Math.abs(data.expectedResult - actualResult);
                boolean passed = difference <= 0.0001;

                // In thông tin cho tất cả test cases
                if (passed) {
                    System.out.printf("Test Case #%d: %s - ✓ PASSED\n", testCaseNumber, data.operation.toUpperCase());
                    System.out.printf("  Input: %.2f %s %.2f\n", data.a, operationSymbol, data.b);
                    System.out.printf("  Expected: %.6f\n", data.expectedResult);
                    System.out.printf("  Actual:   %.6f\n", actualResult);
                    System.out.printf("  Difference: %.6f\n", difference);
                    System.out.println();
                    passedCount++;
                } else {
                    System.out.printf("Test Case #%d: %s - ✗ FAILED\n", testCaseNumber, data.operation.toUpperCase());
                    System.out.printf("  Input: %.2f %s %.2f\n", data.a, operationSymbol, data.b);
                    System.out.printf("  Expected: %.6f\n", data.expectedResult);
                    System.out.printf("  Actual:   %.6f\n", actualResult);
                    System.out.printf("  Difference: %.6f\n", difference);
                    System.out.println();
                    failedCount++;
                }

            } catch (Exception e) {
                failedCount++;
                System.out.printf("Test Case #%d: %s - ✗ FAILED (Exception)\n", testCaseNumber,
                        data.operation.toUpperCase());
                System.out.printf("  Input: %.2f %s %.2f\n", data.a, getOperationSymbol(data.operation), data.b);
                System.out.printf("  Error: %s\n", e.getMessage());
                System.out.println();
            }

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

        // Assert tổng kết - test sẽ fail nếu có test case nào fail
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
     * Lấy ký hiệu toán học cho phép toán
     */
    private String getOperationSymbol(String operation) {
        switch (operation.toLowerCase()) {
            case "add":
                return "+";
            case "subtract":
                return "-";
            case "multiply":
                return "*";
            case "divide":
                return "/";
            default:
                return "?";
        }
    }

    /**
     * Đọc file CSV và trả về danh sách TestData
     */
    private List<TestData> readCsvFile(String resourcePath) {
        List<TestData> testDataList = new ArrayList<>();

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

                String[] values = line.split(",");
                if (values.length != 4) {
                    throw new RuntimeException(
                            String.format("Invalid CSV format at line %d: expected 4 columns, got %d",
                                    lineNumber, values.length));
                }

                try {
                    double a = Double.parseDouble(values[0].trim());
                    double b = Double.parseDouble(values[1].trim());
                    String operation = values[2].trim();
                    double expectedResult = Double.parseDouble(values[3].trim());

                    testDataList.add(new TestData(a, b, operation, expectedResult));
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
    private static class TestData {
        final double a;
        final double b;
        final String operation;
        final double expectedResult;

        TestData(double a, double b, String operation, double expectedResult) {
            this.a = a;
            this.b = b;
            this.operation = operation;
            this.expectedResult = expectedResult;
        }
    }

    @Test
    public void testDivideByZero() {
        Calculator calc = new Calculator();
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0),
                "Should throw ArithmeticException when dividing by zero");
    }

    @Test
    public void testAdd() {
        Calculator calc = new Calculator();
        assertEquals(15.0, calc.add(10, 5), 0.0001);
        assertEquals(0.0, calc.add(-5, 5), 0.0001);
    }

    @Test
    public void testSubtract() {
        Calculator calc = new Calculator();
        assertEquals(5.0, calc.subtract(10, 5), 0.0001);
        assertEquals(-10.0, calc.subtract(-5, 5), 0.0001);
    }

    @Test
    public void testMultiply() {
        Calculator calc = new Calculator();
        assertEquals(50.0, calc.multiply(10, 5), 0.0001);
        assertEquals(0.0, calc.multiply(10, 0), 0.0001);
    }

    @Test
    public void testDivide() {
        Calculator calc = new Calculator();
        assertEquals(2.0, calc.divide(10, 5), 0.0001);
        assertEquals(-2.0, calc.divide(-10, 5), 0.0001);
    }
}
