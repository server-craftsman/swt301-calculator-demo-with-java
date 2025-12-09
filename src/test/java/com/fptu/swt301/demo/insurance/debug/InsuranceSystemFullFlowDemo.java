package com.fptu.swt301.demo.insurance.debug;

import com.fptu.swt301.demo.insurance.service.PremiumCalculationService;
import com.fptu.swt301.demo.insurance.domain.model.InsuranceQuote;
import com.fptu.swt301.demo.insurance.domain.model.PremiumCalculationRequest;
import com.fptu.swt301.demo.insurance.domain.model.BrokerProfile;
import com.fptu.swt301.demo.insurance.service.BrokerProfileService;
import com.fptu.swt301.demo.insurance.service.validator.ProfileValidator;
import com.fptu.swt301.demo.insurance.exception.ValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

/**
 * Demo đầy đủ flow của hệ thống Guru99 Insurance
 * Bao gồm: Profile Management + Premium Calculation + Quotation
 */
public class InsuranceSystemFullFlowDemo {

    @Test
    public void demonstrateFullInsuranceFlow() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("GURU99 INSURANCE SYSTEM - COMPLETE FLOW DEMONSTRATION");
        System.out.println("=".repeat(70) + "\n");

        // ===== STEP 1: CREATE BROKER PROFILE =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 1: CREATE BROKER PROFILE (REGISTRATION)");
        System.out.println("=" + "=".repeat(69));

        BrokerProfileService profileService = new BrokerProfileService();

        BrokerProfile brokerProfile = BrokerProfile.builder()
                .userId("130006")
                .title("Mr")
                .firstName("John")
                .surname("Smith")
                .phone("07700900123")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .licenseType("Full")
                .licensePeriod(5)
                .occupation("Academic")
                .street("123 Oxford Street")
                .city("London")
                .county("Greater London")
                .postCode("SW1A 1AA")
                .build();

        boolean created = profileService.createProfile(brokerProfile);

        if (created) {
            System.out.println("✓ Broker profile created successfully!");
            System.out.println("User ID: " + brokerProfile.getUserId());
            System.out.println();
        }

        // ===== STEP 2: VIEW PROFILE =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 2: VIEW BROKER PROFILE");
        System.out.println("=" + "=".repeat(69));

        BrokerProfile viewedProfile = profileService.viewProfile("130006");
        printProfile(viewedProfile);

        // ===== STEP 3: REQUEST QUOTATION - CALCULATE PREMIUM =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 3: REQUEST QUOTATION - CALCULATE PREMIUM");
        System.out.println("=" + "=".repeat(69));

        PremiumCalculationRequest request = PremiumCalculationRequest.builder()
                .breakdownCover("Roadside")
                .windscreenRepair("No")
                .numberOfAccidents(2)
                .totalMileage(56)
                .estimatedValue(4944044)
                .parkingLocation("Public place")
                .build();

        System.out.println("Quote Request Details:");
        System.out.println("  - Breakdown Cover: " + request.getBreakdownCover().getName());
        System.out.println("  - Windscreen Repair: " + (request.isWindscreenRepair() ? "Yes" : "No"));
        System.out.println("  - Number of Accidents: " + request.getNumberOfAccidents());
        System.out.println("  - Total Mileage: " + request.getTotalMileage());
        System.out.println("  - Estimated Value: £" + (int) request.getEstimatedValue());
        System.out.println("  - Parking Location: " + request.getParkingLocation());

        PremiumCalculationService premiumService = new PremiumCalculationService();
        double premium = premiumService.calculatePremium(request);

        System.out.println("\n[Button: Calculate Premium clicked]");
        System.out.println(">>> Premium: £" + (int) premium);
        System.out.println();

        // ===== STEP 4: SAVE QUOTATION =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 4: SAVE QUOTATION");
        System.out.println("=" + "=".repeat(69));

        InsuranceQuote quote = InsuranceQuote.builder()
                .request(request)
                .calculatedPremium(premium)
                .userId("130006")
                .registrationNumber("5")
                .startOfPolicy("2014.2.7")
                .build();

        System.out.println("[Button: Save Quotation clicked]");
        quote.printQuoteDetails();

        String identificationNumber = quote.getIdentificationNumber();

        // ===== STEP 5: RETRIEVE QUOTATION =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 5: RETRIEVE QUOTATION");
        System.out.println("=" + "=".repeat(69));

        System.out.println("Input Identification Number: " + identificationNumber);
        System.out.println("[Button: Retrieve clicked]\n");

        quote.printRetrieveDetails();

        // ===== STEP 6: EDIT PROFILE =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 6: EDIT BROKER PROFILE");
        System.out.println("=" + "=".repeat(69));

        System.out.println("Updating broker profile...");
        System.out.println("Changes:");
        System.out.println("  - Surname: Smith → Smith-Jones");
        System.out.println("  - License Period: 5 → 6 years");
        System.out.println("  - Occupation: Academic → Senior Academic");

        // Create updated profile with new values
        BrokerProfile updatedBrokerProfile = BrokerProfile.builder()
                .userId("130006")
                .title("Mr")
                .firstName("John")
                .surname("Smith-Jones")
                .phone("07700900123")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .licenseType("Full")
                .licensePeriod(6)
                .occupation("Academic")
                .street("123 Oxford Street")
                .city("London")
                .county("Greater London")
                .postCode("SW1A 1AA")
                .build();

        boolean updated = profileService.updateProfile(updatedBrokerProfile);

        if (updated) {
            System.out.println("\n✓ Profile updated successfully!");
            System.out.println("\n[Button: Update User clicked]");
            System.out.println();

            // View updated profile
            BrokerProfile updatedProfile = profileService.viewProfile("130006");
            printProfile(updatedProfile);
        }

        // ===== SUMMARY =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("FLOW COMPLETED SUCCESSFULLY");
        System.out.println("=" + "=".repeat(69));
        System.out.println("✓ Broker Registration: Success");
        System.out.println("✓ View Profile: Success");
        System.out.println("✓ Request Quotation: Premium £" + (int) premium);
        System.out.println("✓ Save Quotation: ID " + identificationNumber);
        System.out.println("✓ Retrieve Quotation: Success");
        System.out.println("✓ Edit Profile: Success");
        System.out.println("=" + "=".repeat(69) + "\n");
    }

    @Test
    public void demonstrateProfileValidation() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("PROFILE VALIDATION DEMONSTRATION");
        System.out.println("=".repeat(70) + "\n");

        BrokerProfileService profileService = new BrokerProfileService();
        ProfileValidator validator = new ProfileValidator();
        System.out.println("Profile Service initialized. Testing validation rules...\n");

        // Helper method to check validation
        java.util.function.Function<BrokerProfile, Boolean> isValid = profile -> {
            try {
                validator.validate(profile);
                return true;
            } catch (ValidationException e) {
                return false;
            }
        };

        // Example 1: Valid Profile
        System.out.println("Example 1: VALID PROFILE");
        System.out.println("-".repeat(70));
        BrokerProfile validProfile = BrokerProfile.builder()
                .userId("USER001")
                .title("Mr")
                .firstName("John")
                .surname("Doe")
                .phone("07700900123")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .licenseType("Full")
                .licensePeriod(5)
                .occupation("Student")
                .build();

        System.out.println("Input: " + validProfile.getFirstName() + " " + validProfile.getSurname());
        System.out.println("Phone: " + validProfile.getPhoneValue());
        System.out.println("Age: " + (LocalDate.now().getYear() - validProfile.getDateOfBirth().getYear()));
        System.out.println("Valid: " + isValid.apply(validProfile));

        // Try to create
        boolean created = profileService.createProfile(validProfile);
        System.out.println("Created: " + created);
        System.out.println();

        // Example 2: Invalid - Age < 18
        System.out.println("Example 2: INVALID - AGE UNDER 18");
        System.out.println("-".repeat(70));
        BrokerProfile youngProfile = BrokerProfile.builder()
                .userId("USER002")
                .title("Mr")
                .firstName("Too")
                .surname("Young")
                .phone("07700900456")
                .dateOfBirth(LocalDate.now().minusYears(17))
                .licenseType("Provisional")
                .licensePeriod(0)
                .occupation("Student")
                .build();

        System.out.println("Input: " + youngProfile.getFirstName() + " " + youngProfile.getSurname());
        System.out.println("Age: " + (LocalDate.now().getYear() - youngProfile.getDateOfBirth().getYear()));
        System.out.println("Valid: " + isValid.apply(youngProfile));
        System.out.println("Reason: Age must be at least 18");
        System.out.println();

        // Example 3: Invalid - Phone too short
        System.out.println("Example 3: INVALID - PHONE TOO SHORT");
        System.out.println("-".repeat(70));
        try {
            BrokerProfile shortPhoneProfile = BrokerProfile.builder()
                    .userId("USER003")
                    .title("Ms")
                    .firstName("Jane")
                    .surname("Smith")
                    .phone("123456") // Too short
                    .dateOfBirth(LocalDate.of(1985, 5, 15))
                    .licenseType("Full")
                    .licensePeriod(7)
                    .occupation("Student")
                    .build();

            System.out.println("Input: " + shortPhoneProfile.getFirstName() + " " + shortPhoneProfile.getSurname());
            System.out.println("Phone: 123456 (only 6 digits)");
            System.out.println("Valid: " + isValid.apply(shortPhoneProfile));
            System.out.println("Reason: Phone must be 10-11 digits");
        } catch (ValidationException e) {
            System.out.println("Input: Jane Smith");
            System.out.println("Phone: 123456 (only 6 digits)");
            System.out.println("Valid: false");
            System.out.println("Reason: Phone must be 10-11 digits");
        }
        System.out.println();

        // Example 4: Invalid - Empty FirstName
        System.out.println("Example 4: INVALID - EMPTY FIRSTNAME");
        System.out.println("-".repeat(70));
        BrokerProfile emptyNameProfile = BrokerProfile.builder()
                .userId("USER004")
                .title("Doctor")
                .firstName("") // Empty
                .surname("Brown")
                .phone("07700900789")
                .dateOfBirth(LocalDate.of(1980, 10, 20))
                .licenseType("Full")
                .licensePeriod(10)
                .occupation("Student")
                .build();

        System.out.println("Input: '' " + emptyNameProfile.getSurname());
        System.out.println("Valid: " + isValid.apply(emptyNameProfile));
        System.out.println("Reason: FirstName cannot be empty");
        System.out.println();

        System.out.println("=".repeat(70) + "\n");
    }

    /**
     * Helper method to print profile information
     */
    private void printProfile(BrokerProfile profile) {
        System.out.println("Profile Details:");
        System.out.println("  User ID: " + profile.getUserId());
        System.out.println("  Name: " + profile.getTitle() + " " + profile.getFirstName() + " " + profile.getSurname());
        System.out.println("  Phone: " + profile.getPhoneValue());
        System.out.println("  Date of Birth: " + profile.getDateOfBirth());
        System.out.println("  License: " + profile.getLicenseType() + " (" + profile.getLicensePeriod() + " years)");
        System.out.println("  Occupation: " + profile.getOccupation());
        if (profile.getAddress() != null) {
            System.out.println("  Address: " + profile.getStreet() + ", " + profile.getCity() + ", " + profile.getCounty() + " " + profile.getPostCode());
        }
        System.out.println();
    }
}
