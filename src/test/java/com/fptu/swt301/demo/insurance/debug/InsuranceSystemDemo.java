package com.fptu.swt301.demo.insurance.debug;

import com.fptu.swt301.demo.insurance.calculator.InsurancePremiumCalculator;
import com.fptu.swt301.demo.insurance.calculator.InsuranceQuote;
import com.fptu.swt301.demo.insurance.calculator.InsuranceQuoteRequest;
import org.junit.jupiter.api.Test;

/**
 * Demo class minh họa cách sử dụng Insurance Premium Calculator
 * và flow của hệ thống Guru99 Insurance
 */
public class InsuranceSystemDemo {

    @Test
    public void demonstrateInsuranceFlow() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("GURU99 INSURANCE SYSTEM - DEMO FLOW");
        System.out.println("=".repeat(60) + "\n");

        // Step 1: Request Quotation - Calculate Premium
        System.out.println("STEP 1: REQUEST QUOTATION");
        System.out.println("-".repeat(60));

        InsuranceQuoteRequest request = new InsuranceQuoteRequest();
        request.setBreakdownCover("Roadside");
        request.setWindscreenRepair("No");
        request.setNumberOfAccidents(2);
        request.setTotalMileage(56);
        request.setEstimatedValue(4944044);
        request.setParkingLocation("Public place");
        request.setRegistrationNumber("5");
        request.setStartOfPolicy("2014.2.7");

        System.out.println("Input Data:");
        System.out.println("  - Breakdown Cover: " + request.getBreakdownCover());
        System.out.println("  - Windscreen Repair: " + request.getWindscreenRepair());
        System.out.println("  - Number of Accidents: " + request.getNumberOfAccidents());
        System.out.println("  - Total Mileage: " + request.getTotalMileage());
        System.out.println("  - Estimated Value: £" + (int) request.getEstimatedValue());
        System.out.println("  - Parking Location: " + request.getParkingLocation());
        System.out.println("  - Registration: " + request.getRegistrationNumber());
        System.out.println("  - Start of Policy: " + request.getStartOfPolicy());

        // Calculate Premium
        InsurancePremiumCalculator calculator = new InsurancePremiumCalculator();
        double premium = calculator.calculatePremium(request);

        System.out.println("\n[Button: Calculate Premium clicked]");
        System.out.println(">>> Premium: £" + (int) premium);

        // Step 2: Save Quotation
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STEP 2: SAVE QUOTATION");
        System.out.println("-".repeat(60));

        InsuranceQuote quote = new InsuranceQuote(request, premium);
        quote.setUserId("130006");

        System.out.println("[Button: Save Quotation clicked]");
        quote.printQuoteDetails();

        String identificationNumber = quote.getIdentificationNumber();

        // Step 3: Retrieve Quotation
        System.out.println("=".repeat(60));
        System.out.println("STEP 3: RETRIEVE QUOTATION");
        System.out.println("-".repeat(60));

        System.out.println("Input Identification Number: " + identificationNumber);
        System.out.println("[Button: Retrieve clicked]\n");

        // Simulate retrieve
        quote.printRetrieveDetails();

        // Step 4: Profile
        System.out.println("=".repeat(60));
        System.out.println("STEP 4: VIEW PROFILE");
        System.out.println("-".repeat(60));
        System.out.println("Title: Mr");
        System.out.println("Firstname: John");
        System.out.println("Surname: Smith");
        System.out.println("Phone: 0123456789");
        System.out.println("Date of Birth: 1989-01-08");
        System.out.println("License type: Full");
        System.out.println("License period: 2 years");
        System.out.println("Occupation: Academic");
        System.out.println("ADDRESS:");
        System.out.println("  Street: 123 Main St");
        System.out.println("  City: London");
        System.out.println("  County: Greater London");
        System.out.println("  Post code: SW1A 1AA");
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("DEMO COMPLETED");
        System.out.println("=".repeat(60) + "\n");
    }

    @Test
    public void demonstratePremiumCalculationExamples() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PREMIUM CALCULATION EXAMPLES");
        System.out.println("=".repeat(60) + "\n");

        InsurancePremiumCalculator calculator = new InsurancePremiumCalculator();

        // Example 1: Minimal Premium
        System.out.println("Example 1: MINIMAL PREMIUM");
        System.out.println("-".repeat(60));
        double premium1 = calculator.calculatePremium("No cover", "No", 0, 3000, 5000, "Driveway/Carport");
        System.out.println("Input: No cover, No windscreen, 0 accidents, 3000 miles, Safe parking");
        System.out.println("Calculation: £1000 * 1.01 * 0.7 = £707");
        System.out.println("Premium: £" + premium1);
        System.out.println();

        // Example 2: With Windscreen
        System.out.println("Example 2: WITH WINDSCREEN REPAIR");
        System.out.println("-".repeat(60));
        double premium2 = calculator.calculatePremium("No cover", "Yes", 0, 3000, 5000, "Driveway/Carport");
        System.out.println("Input: No cover, YES windscreen, 0 accidents, 3000 miles, Safe parking");
        System.out.println("Calculation: £1000 * 1.01 * 0.7 + £30 = £737");
        System.out.println("Premium: £" + premium2);
        System.out.println();

        // Example 3: High Mileage
        System.out.println("Example 3: HIGH MILEAGE");
        System.out.println("-".repeat(60));
        double premium3 = calculator.calculatePremium("No cover", "No", 0, 6000, 5000, "Driveway/Carport");
        System.out.println("Input: No cover, No windscreen, 0 accidents, 6000 miles, Safe parking");
        System.out.println("Calculation: £1000 * 1.01 * 0.7 + £50 = £757");
        System.out.println("Premium: £" + premium3);
        System.out.println();

        // Example 4: Public Parking
        System.out.println("Example 4: PUBLIC PARKING");
        System.out.println("-".repeat(60));
        double premium4 = calculator.calculatePremium("No cover", "No", 0, 3000, 5000, "Public Place");
        System.out.println("Input: No cover, No windscreen, 0 accidents, 3000 miles, PUBLIC parking");
        System.out.println("Calculation: £1000 * 1.01 * 0.7 + £30 = £737");
        System.out.println("Premium: £" + premium4);
        System.out.println();

        // Example 5: European Cover
        System.out.println("Example 5: EUROPEAN COVER");
        System.out.println("-".repeat(60));
        double premium5 = calculator.calculatePremium("European", "No", 0, 3000, 5000, "Driveway/Carport");
        System.out.println("Input: EUROPEAN cover, No windscreen, 0 accidents, 3000 miles, Safe parking");
        System.out.println("Calculation: £1000 * 1.04 * 0.7 = £728");
        System.out.println("Premium: £" + premium5);
        System.out.println();

        // Example 6: With Accident (No Discount)
        System.out.println("Example 6: WITH ACCIDENT (NO DISCOUNT)");
        System.out.println("-".repeat(60));
        double premium6 = calculator.calculatePremium("No cover", "No", 1, 3000, 5000, "Driveway/Carport");
        System.out.println("Input: No cover, No windscreen, 1 ACCIDENT, 3000 miles, Safe parking");
        System.out.println("Calculation: £1000 * 1.01 (NO 30% discount) = £1010");
        System.out.println("Premium: £" + premium6);
        System.out.println();

        // Example 7: Maximum Premium
        System.out.println("Example 7: MAXIMUM PREMIUM");
        System.out.println("-".repeat(60));
        double premium7 = calculator.calculatePremium("European", "Yes", 0, 10000, 50000, "Public Place");
        System.out.println("Input: European, YES windscreen, 0 accidents, 10000 miles, Public parking");
        System.out.println("Calculation: £1000 * 1.04 * 0.7 + £30 + £50 + £30 = £838");
        System.out.println("Premium: £" + premium7);
        System.out.println();

        System.out.println("=".repeat(60) + "\n");
    }
}
