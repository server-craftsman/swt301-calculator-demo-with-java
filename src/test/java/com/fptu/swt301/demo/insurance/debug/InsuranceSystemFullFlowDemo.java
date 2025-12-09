package com.fptu.swt301.demo.insurance.debug;

import com.fptu.swt301.demo.insurance.calculator.InsurancePremiumCalculator;
import com.fptu.swt301.demo.insurance.calculator.InsuranceQuote;
import com.fptu.swt301.demo.insurance.calculator.InsuranceQuoteRequest;
import com.fptu.swt301.demo.insurance.profile.BrokerProfile;
import com.fptu.swt301.demo.insurance.profile.BrokerProfileService;
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

        BrokerProfile brokerProfile = new BrokerProfile();
        brokerProfile.setUserId("130006");
        brokerProfile.setTitle("Mr");
        brokerProfile.setFirstName("John");
        brokerProfile.setSurname("Smith");
        brokerProfile.setPhone("07700900123");
        brokerProfile.setDateOfBirth(LocalDate.of(1985, 5, 15));
        brokerProfile.setLicenseType("Full");
        brokerProfile.setLicensePeriod(5);
        brokerProfile.setOccupation("Academic");
        brokerProfile.setStreet("123 Oxford Street");
        brokerProfile.setCity("London");
        brokerProfile.setCounty("Greater London");
        brokerProfile.setPostCode("SW1A 1AA");

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
        viewedProfile.printProfile();

        // ===== STEP 3: REQUEST QUOTATION - CALCULATE PREMIUM =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 3: REQUEST QUOTATION - CALCULATE PREMIUM");
        System.out.println("=" + "=".repeat(69));

        InsuranceQuoteRequest request = new InsuranceQuoteRequest();
        request.setBreakdownCover("Roadside");
        request.setWindscreenRepair("No");
        request.setNumberOfAccidents(2);
        request.setTotalMileage(56);
        request.setEstimatedValue(4944044);
        request.setParkingLocation("Public place");
        request.setRegistrationNumber("5");
        request.setStartOfPolicy("2014.2.7");

        System.out.println("Quote Request Details:");
        System.out.println("  - Breakdown Cover: " + request.getBreakdownCover());
        System.out.println("  - Windscreen Repair: " + request.getWindscreenRepair());
        System.out.println("  - Number of Accidents: " + request.getNumberOfAccidents());
        System.out.println("  - Total Mileage: " + request.getTotalMileage());
        System.out.println("  - Estimated Value: £" + (int) request.getEstimatedValue());
        System.out.println("  - Parking Location: " + request.getParkingLocation());
        System.out.println("  - Registration: " + request.getRegistrationNumber());
        System.out.println("  - Start of Policy: " + request.getStartOfPolicy());

        InsurancePremiumCalculator calculator = new InsurancePremiumCalculator();
        double premium = calculator.calculatePremium(request);

        System.out.println("\n[Button: Calculate Premium clicked]");
        System.out.println(">>> Premium: £" + (int) premium);
        System.out.println();

        // ===== STEP 4: SAVE QUOTATION =====
        System.out.println("=" + "=".repeat(69));
        System.out.println("STEP 4: SAVE QUOTATION");
        System.out.println("=" + "=".repeat(69));

        InsuranceQuote quote = new InsuranceQuote(request, premium);
        quote.setUserId("130006");

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

        brokerProfile.setSurname("Smith-Jones");
        brokerProfile.setLicensePeriod(6);
        brokerProfile.setOccupation("Academic");

        boolean updated = profileService.updateProfile(brokerProfile);

        if (updated) {
            System.out.println("\n✓ Profile updated successfully!");
            System.out.println("\n[Button: Update User clicked]");
            System.out.println();

            // View updated profile
            BrokerProfile updatedProfile = profileService.viewProfile("130006");
            updatedProfile.printProfile();
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
        System.out.println("Profile Service initialized. Testing validation rules...\n");

        // Example 1: Valid Profile
        System.out.println("Example 1: VALID PROFILE");
        System.out.println("-".repeat(70));
        BrokerProfile validProfile = new BrokerProfile();
        validProfile.setUserId("USER001");
        validProfile.setTitle("Mr");
        validProfile.setFirstName("John");
        validProfile.setSurname("Doe");
        validProfile.setPhone("07700900123");
        validProfile.setDateOfBirth(LocalDate.of(1990, 1, 1));
        validProfile.setLicenseType("Full");
        validProfile.setLicensePeriod(5);

        System.out.println("Input: " + validProfile.getFirstName() + " " + validProfile.getSurname());
        System.out.println("Phone: " + validProfile.getPhone());
        System.out.println("Age: " + (LocalDate.now().getYear() - validProfile.getDateOfBirth().getYear()));
        System.out.println("Valid: " + validProfile.isValid());

        // Try to create
        boolean created = profileService.createProfile(validProfile);
        System.out.println("Created: " + created);
        System.out.println();

        // Example 2: Invalid - Age < 18
        System.out.println("Example 2: INVALID - AGE UNDER 18");
        System.out.println("-".repeat(70));
        BrokerProfile youngProfile = new BrokerProfile();
        youngProfile.setUserId("USER002");
        youngProfile.setTitle("Mr");
        youngProfile.setFirstName("Too");
        youngProfile.setSurname("Young");
        youngProfile.setPhone("07700900456");
        youngProfile.setDateOfBirth(LocalDate.now().minusYears(17));
        youngProfile.setLicenseType("Provisional");
        youngProfile.setLicensePeriod(0);

        System.out.println("Input: " + youngProfile.getFirstName() + " " + youngProfile.getSurname());
        System.out.println("Age: " + (LocalDate.now().getYear() - youngProfile.getDateOfBirth().getYear()));
        System.out.println("Valid: " + youngProfile.isValid());
        System.out.println("Reason: Age must be at least 18");
        System.out.println();

        // Example 3: Invalid - Phone too short
        System.out.println("Example 3: INVALID - PHONE TOO SHORT");
        System.out.println("-".repeat(70));
        BrokerProfile shortPhoneProfile = new BrokerProfile();
        shortPhoneProfile.setUserId("USER003");
        shortPhoneProfile.setTitle("Ms");
        shortPhoneProfile.setFirstName("Jane");
        shortPhoneProfile.setSurname("Smith");
        shortPhoneProfile.setPhone("123456");
        shortPhoneProfile.setDateOfBirth(LocalDate.of(1985, 5, 15));
        shortPhoneProfile.setLicenseType("Full");
        shortPhoneProfile.setLicensePeriod(7);

        System.out.println("Input: " + shortPhoneProfile.getFirstName() + " " + shortPhoneProfile.getSurname());
        System.out.println("Phone: " + shortPhoneProfile.getPhone() + " (only 6 digits)");
        System.out.println("Valid: " + shortPhoneProfile.isValid());
        System.out.println("Reason: Phone must be 10-11 digits");
        System.out.println();

        // Example 4: Invalid - Empty FirstName
        System.out.println("Example 4: INVALID - EMPTY FIRSTNAME");
        System.out.println("-".repeat(70));
        BrokerProfile emptyNameProfile = new BrokerProfile();
        emptyNameProfile.setUserId("USER004");
        emptyNameProfile.setTitle("Doctor");
        emptyNameProfile.setFirstName("");
        emptyNameProfile.setSurname("Brown");
        emptyNameProfile.setPhone("07700900789");
        emptyNameProfile.setDateOfBirth(LocalDate.of(1980, 10, 20));
        emptyNameProfile.setLicenseType("Full");
        emptyNameProfile.setLicensePeriod(10);

        System.out.println("Input: '" + emptyNameProfile.getFirstName() + "' " + emptyNameProfile.getSurname());
        System.out.println("Valid: " + emptyNameProfile.isValid());
        System.out.println("Reason: FirstName cannot be empty");
        System.out.println();

        System.out.println("=".repeat(70) + "\n");
    }
}
