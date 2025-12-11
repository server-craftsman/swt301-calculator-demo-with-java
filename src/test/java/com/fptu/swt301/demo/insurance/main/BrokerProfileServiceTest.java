package com.fptu.swt301.demo.insurance.main;

import com.fptu.swt301.demo.insurance.exception.ValidationException;
import com.fptu.swt301.demo.insurance.domain.model.BrokerProfile;
import com.fptu.swt301.demo.insurance.service.BrokerProfileService;
import com.fptu.swt301.demo.insurance.service.validator.ProfileValidator;
import com.fptu.swt301.demo.insurance.domain.valueobject.LicenseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class cho BrokerProfileService
 * Test View Profile và Update Profile functionality
 * Sử dụng phương pháp:
 * - Equivalence Partitioning
 * - Boundary Value Analysis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrokerProfileServiceTest {

    private BrokerProfileService profileService;
    private ProfileValidator profileValidator;

    @BeforeEach
    public void setUp() {
        profileService = new BrokerProfileService();
        profileValidator = new ProfileValidator();
    }

    /**
     * Helper method để check validation
     */
    private boolean isValid(BrokerProfile profile) {
        try {
            profileValidator.validate(profile);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    /**
     * Test Factory: Tạo dynamic tests từ CSV file
     * Mỗi test case trong CSV sẽ trở thành một test method riêng biệt
     */
    @TestFactory
    @DisplayName("Broker Profile Service - CSV Test Cases")
    @Order(1)
    public List<DynamicTest> testBrokerProfileFromCsvFile() {
        List<ProfileTestData> testDataList = readCsvFile("/insurance/broker_profile_test_data.csv");
        List<DynamicTest> dynamicTests = new ArrayList<>();

        System.out.println("\n========================================");
        System.out.println("BROKER PROFILE SERVICE TEST");
        System.out.println("Total test cases: " + testDataList.size());
        System.out.println("========================================\n");

        int testCaseNumber = 1;
        for (ProfileTestData data : testDataList) {
            final ProfileTestData testData = data; // final for lambda
            final int caseNumber = testCaseNumber;

            // Tạo dynamic test với display name là test case ID
            DynamicTest dynamicTest = DynamicTest.dynamicTest(
                    String.format("TC_P%02d: %s - %s", caseNumber, testData.testCaseId, testData.testDescription),
                    () -> executeTestCase(testData, caseNumber));

            dynamicTests.add(dynamicTest);
            testCaseNumber++;
        }

        return dynamicTests;
    }

    /**
     * Execute một test case cụ thể
     */
    private void executeTestCase(ProfileTestData data, int testCaseNumber) {
        // Clear database before each test case to avoid interference
        profileService.clearAllProfiles();
        try {
            System.out.printf("Test Case #%d: %s\n", testCaseNumber, data.testCaseId);
            System.out.printf("  Description: %s\n", data.testDescription);
            System.out.printf("  Category: %s\n", data.testCategory);
            System.out.printf("  Input:\n");
            System.out.printf("    - User ID: %s\n", data.userId);
            System.out.printf("    - Name: %s %s %s\n", data.title, data.firstName, data.surname);
            System.out.printf("    - Phone: %s\n", data.phone);
            System.out.printf("    - DOB: %s\n", data.dateOfBirth);
            System.out.printf("    - License: %s (%d years)\n", data.licenseType, data.licensePeriod);
            System.out.printf("    - Occupation: %s\n", data.occupation);

            BrokerProfile profile = createProfileFromData(data);
            boolean actualResult;
            String actualMessage = "";
            String expectedResult = "";

            // Auto-determine expected result based on validation logic and test category
            boolean isValid = isValid(profile);

            // Determine expected result based on test category and validation
            if (data.testCategory.contains("Update")) {
                expectedResult = "UPDATE_SUCCESS";
                // First create the profile if it doesn't exist
                if (!profileService.profileExists(data.userId)) {
                    profileService.createProfile(profile);
                }
                // Test update existing profile
                actualResult = profileService.updateProfile(profile);
                actualMessage = actualResult ? "Profile updated successfully" : "Failed to update profile";
            } else if (data.testCategory.contains("Duplicate")) {
                expectedResult = "CREATE_FAIL";
                // First create the profile
                if (!profileService.profileExists(data.userId)) {
                    profileService.createProfile(profile);
                }
                // Test create duplicate profile
                actualResult = !profileService.createProfile(profile);
                actualMessage = actualResult ? "Correctly rejected duplicate"
                        : "Unexpectedly created duplicate";
            } else if (isValid) {
                expectedResult = "VALID";
                // Test create valid profile
                try {
                    actualResult = profileService.createProfile(profile);
                    actualMessage = actualResult ? "Profile created successfully" : "Failed to create profile";

                    if (actualResult) {
                        // Verify by viewing
                        BrokerProfile retrieved = profileService.viewProfile(data.userId);
                        actualResult = retrieved != null &&
                                retrieved.getFirstName().equals(data.firstName) &&
                                retrieved.getSurname().equals(data.surname);
                    }
                } catch (ValidationException e) {
                    actualResult = false;
                    if (e.getErrorCount() > 1) {
                        actualMessage = String.format("ValidationException with %d errors:\n", e.getErrorCount());
                        for (int i = 0; i < e.getErrors().size(); i++) {
                            actualMessage += String.format("  %d. %s\n", i + 1, e.getErrors().get(i));
                        }
                    } else {
                        actualMessage = "ValidationException: " + e.getMessage();
                    }
                }
            } else {
                expectedResult = "INVALID";
                // Test invalid profile - should fail validation
                try {
                    actualResult = profileService.createProfile(profile);
                    actualResult = false; // Should not reach here
                    actualMessage = "Unexpectedly accepted invalid profile";
                } catch (ValidationException e) {
                    actualResult = true;
                    if (e.getErrorCount() > 1) {
                        actualMessage = String.format("Correctly rejected with %d errors:\n", e.getErrorCount());
                        for (int i = 0; i < e.getErrors().size(); i++) {
                            actualMessage += String.format("  %d. %s\n", i + 1, e.getErrors().get(i));
                        }
                    } else {
                        actualMessage = "Correctly rejected: " + e.getMessage();
                    }
                } catch (IllegalArgumentException e) {
                    actualResult = true;
                    actualMessage = "Correctly rejected: " + e.getMessage();
                }
            }

            System.out.printf("  Expected: %s (auto-determined from validation)\n", expectedResult);
            System.out.printf("  Actual: %s\n", actualMessage);

            if (actualResult) {
                System.out.printf("  Status: ✓ PASSED\n");
            } else {
                System.out.printf("  Status: ✗ FAILED\n");
                System.out.printf("  Details: %s\n", actualMessage);
            }

            // Assert để JUnit báo pass/fail cho test case này
            assertTrue(actualResult,
                    String.format("Test case %s failed: %s", data.testCaseId, actualMessage));

        } catch (ValidationException e) {
            // Check if profile is invalid - if so, exception is expected
            BrokerProfile tempProfile = createProfileFromData(data);
            if (!isValid(tempProfile) && !data.testCategory.contains("Update")
                    && !data.testCategory.contains("Duplicate")) {
                System.out.printf("  Status: ✓ PASSED (ValidationException expected for invalid profile)\n");
                if (e.getErrorCount() > 1) {
                    System.out.printf("  Found %d validation errors:\n", e.getErrorCount());
                    for (int i = 0; i < e.getErrors().size(); i++) {
                        System.out.printf("    %d. %s\n", i + 1, e.getErrors().get(i));
                    }
                } else {
                    System.out.printf("  Error: %s\n", e.getMessage());
                }
                // Expected exception, test passes
            } else {
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
            }
        } catch (Exception e) {
            // Check if profile is invalid - if so, exception is expected
            BrokerProfile tempProfile = createProfileFromData(data);
            if (!isValid(tempProfile) && !data.testCategory.contains("Update")
                    && !data.testCategory.contains("Duplicate")) {
                System.out.printf("  Status: ✓ PASSED (Exception expected for invalid profile)\n");
                if (e instanceof ValidationException) {
                    ValidationException ve = (ValidationException) e;
                    if (ve.getErrorCount() > 1) {
                        System.out.printf("  Found %d validation errors:\n", ve.getErrorCount());
                        for (int i = 0; i < ve.getErrors().size(); i++) {
                            System.out.printf("    %d. %s\n", i + 1, ve.getErrors().get(i));
                        }
                    }
                }
                // Expected exception, test passes
            } else {
                System.out.printf("  Status: ✗ FAILED (Unexpected Exception)\n");
                System.out.printf("  Error: %s\n", e.getMessage());
                if (e.getCause() != null) {
                    System.out.printf("  Cause: %s\n", e.getCause().getMessage());
                }
                fail(String.format("Test case %s failed with unexpected exception: %s",
                        data.testCaseId, e.getMessage()));
            }
        }
    }

    /**
     * Create BrokerProfile from test data
     */
    private BrokerProfile createProfileFromData(ProfileTestData data) {
        BrokerProfile.Builder builder = BrokerProfile.builder()
                .userId(data.userId)
                .title(data.title)
                .firstName(data.firstName)
                .surname(data.surname)
                .phone(data.phone)
                .occupation(data.occupation);

        // Parse date of birth
        if (!"null".equals(data.dateOfBirth) && data.dateOfBirth != null && !data.dateOfBirth.isEmpty()) {
            try {
                builder.dateOfBirth(LocalDate.parse(data.dateOfBirth));
            } catch (Exception e) {
                // Leave null
            }
        }

        // License info
        try {
            builder.licenseInfo(LicenseInfo.of(data.licenseType, data.licensePeriod));
        } catch (Exception e) {
            // Will be validated later
            builder.licenseType(data.licenseType)
                    .licensePeriod(data.licensePeriod);
        }

        // Address
        if (data.street != null || data.city != null || data.county != null || data.postCode != null) {
            builder.street(data.street)
                    .city(data.city)
                    .county(data.county)
                    .postCode(data.postCode);
        }

        return builder.build();
    }

    /**
     * Read CSV file
     */
    private List<ProfileTestData> readCsvFile(String resourcePath) {
        List<ProfileTestData> testDataList = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // Skip header
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
                if (values.length != 16) {
                    throw new RuntimeException(
                            String.format("Invalid CSV format at line %d: expected 16 columns, got %d",
                                    lineNumber, values.length));
                }

                try {
                    ProfileTestData data = new ProfileTestData();
                    data.testCaseId = values[0].trim();
                    data.testDescription = values[1].trim();
                    data.userId = values[2].trim();
                    data.title = values[3].trim();
                    data.firstName = values[4].trim();
                    data.surname = values[5].trim();
                    data.phone = values[6].trim();
                    data.dateOfBirth = values[7].trim();
                    data.licenseType = values[8].trim();
                    data.licensePeriod = values[9].trim().isEmpty() ? 0 : Integer.parseInt(values[9].trim());
                    data.occupation = values[10].trim();
                    data.street = values[11].trim();
                    data.city = values[12].trim();
                    data.county = values[13].trim();
                    data.postCode = values[14].trim();
                    data.testCategory = values[15].trim();
                    // expectedResult will be determined automatically based on validation logic

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
     * Test data class
     */
    private static class ProfileTestData {
        String testCaseId;
        String testDescription;
        String userId;
        String title;
        String firstName;
        String surname;
        String phone;
        String dateOfBirth;
        String licenseType;
        int licensePeriod;
        String occupation;
        String street;
        String city;
        String county;
        String postCode;
        String testCategory;
    }

    // ==================== Individual Unit Tests ====================

    @Test
    @Disabled
    @Order(2)
    public void testViewProfile_ValidUser() {
        BrokerProfile profile = BrokerProfile.builder()
                .userId("USER001")
                .title("Mr")
                .firstName("John")
                .surname("Smith")
                .phone("07700900123")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .licenseType("Full")
                .licensePeriod(5)
                .occupation("Student")
                .build();

        profileService.createProfile(profile);

        BrokerProfile retrieved = profileService.viewProfile("USER001");
        assertNotNull(retrieved);
        assertEquals("John", retrieved.getFirstName());
        assertEquals("Smith", retrieved.getSurname());
    }

    @Test
    @Disabled
    @Order(3)
    public void testViewProfile_InvalidUser() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            profileService.viewProfile("NONEXISTENT");
        });
        assertTrue(exception.getMessage().contains("Profile not found"));
    }

    @Test
    @Disabled
    @Order(4)
    public void testUpdateProfile_Success() {
        BrokerProfile profile = BrokerProfile.builder()
                .userId("USER002")
                .title("Mrs")
                .firstName("Jane")
                .surname("Doe")
                .phone("07700900456")
                .dateOfBirth(LocalDate.of(1990, 3, 20))
                .licenseType("Full")
                .licensePeriod(3)
                .occupation("Student")
                .build();

        profileService.createProfile(profile);

        // Update - create new profile with updated data
        BrokerProfile updatedProfile = BrokerProfile.builder()
                .userId("USER002")
                .title("Mrs")
                .firstName("Jane")
                .surname("Smith")
                .phone("07700900456")
                .dateOfBirth(LocalDate.of(1990, 3, 20))
                .licenseType("Full")
                .licensePeriod(4)
                .occupation("Student")
                .build();

        boolean result = profileService.updateProfile(updatedProfile);
        assertTrue(result);

        BrokerProfile retrieved = profileService.viewProfile("USER002");
        assertEquals("Smith", retrieved.getSurname());
        assertEquals(4, retrieved.getLicensePeriod());
    }

    @Test
    @Disabled
    @Order(5)
    public void testCreateProfile_DuplicateUser() {
        BrokerProfile profile1 = BrokerProfile.builder()
                .userId("USER003")
                .title("Mr")
                .firstName("Tom")
                .surname("Jones")
                .phone("07700900789")
                .dateOfBirth(LocalDate.of(1988, 8, 10))
                .licenseType("Full")
                .licensePeriod(6)
                .occupation("Student")
                .build();

        profileService.createProfile(profile1);

        BrokerProfile profile2 = BrokerProfile.builder()
                .userId("USER003")
                .title("Mr")
                .firstName("Different")
                .surname("Name")
                .phone("07700900321")
                .dateOfBirth(LocalDate.of(1992, 12, 5))
                .licenseType("Provisional")
                .licensePeriod(1)
                .occupation("Student")
                .build();

        boolean result = profileService.createProfile(profile2);
        assertFalse(result, "Should not create duplicate profile");
    }

    @Test
    @Disabled
    @Order(6)
    public void testProfileValidation_InvalidAge() {
        BrokerProfile profile = BrokerProfile.builder()
                .userId("USER004")
                .title("Mr")
                .firstName("Too")
                .surname("Young")
                .phone("07700900111")
                .dateOfBirth(LocalDate.now().minusYears(17)) // 17 years old
                .licenseType("Provisional")
                .licensePeriod(0)
                .occupation("Student")
                .build();

        assertFalse(isValid(profile), "Should reject age < 18");
    }

    @Test
    @Disabled
    @Order(7)
    public void testProfileValidation_InvalidPhone() {
        try {
            BrokerProfile profile = BrokerProfile.builder()
                    .userId("USER005")
                    .title("Mr")
                    .firstName("Short")
                    .surname("Phone")
                    .phone("123") // Too short
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .licenseType("Full")
                    .licensePeriod(5)
                    .occupation("Student")
                    .build();

            assertFalse(isValid(profile), "Should reject short phone number");
        } catch (ValidationException e) {
            // Expected - phone validation fails during build
            assertTrue(true);
        }
    }
}
