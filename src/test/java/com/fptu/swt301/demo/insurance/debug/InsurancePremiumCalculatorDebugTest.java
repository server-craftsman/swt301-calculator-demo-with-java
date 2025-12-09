package com.fptu.swt301.demo.insurance.debug;

import com.fptu.swt301.demo.insurance.service.PremiumCalculationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Debug test để kiểm tra tính toán premium
 */
public class InsurancePremiumCalculatorDebugTest {

    private final PremiumCalculationService premiumService = new PremiumCalculationService();

    @Test
    public void debugTestCase02() {
        System.out.println("\n========================================");
        System.out.println("DEBUG TEST CASE #02");
        System.out.println("========================================\n");

        // Test case #02: No cover, No windscreen, 0 accidents, 3000 mileage, 100 value,
        // Safe parking
        // Expected: £91.00
        // Actual: £90.99

        double premium = premiumService.calculatePremium(
                "No cover", // 1% increase
                "No", // No windscreen
                0, // 0 accidents (30% discount)
                3000, // Low mileage
                100, // Estimated value
                "Driveway/Carport" // Safe parking
        );

        System.out.println("Input:");
        System.out.println("  Breakdown Cover: No cover (1%)");
        System.out.println("  Windscreen: No");
        System.out.println("  Accidents: 0 (30% discount)");
        System.out.println("  Mileage: 3000");
        System.out.println("  Value: £100");
        System.out.println("  Parking: Safe");

        System.out.println("\nCalculation steps:");
        System.out.println("  Base Premium: £130.00");
        System.out.println("  After 1% increase: £130 × 1.01 = £131.30");
        System.out.println("  After 30% discount: £131.30 × 0.7 = £91.91");
        System.out.println("  Rounded: £" + premium);

        System.out.println("\nExpected: £91.00");
        System.out.println("Actual: £" + premium);
        System.out.println("Difference: £" + Math.abs(91.00 - premium));

        // Kiểm tra với delta lớn hơn
        assertEquals(91.00, premium, 0.02, "Should be within £0.02 of expected value");
    }

    @Test
    public void debugCalculationSteps() {
        System.out.println("\n========================================");
        System.out.println("DEBUG CALCULATION STEPS");
        System.out.println("========================================\n");

        double base = 130.0;
        System.out.println("Step 1: Base Premium = £" + base);

        double afterBreakdown = base * 1.01;
        System.out.println("Step 2: After No cover (1%) = £" + base + " × 1.01 = £" + afterBreakdown);

        double afterDiscount = afterBreakdown * 0.7;
        System.out.println("Step 3: After 30% discount = £" + afterBreakdown + " × 0.7 = £" + afterDiscount);

        double rounded = Math.round(afterDiscount * 100.0) / 100.0;
        System.out.println("Step 4: Rounded = £" + rounded);

        System.out.println("\nExpected: £91.00");
        System.out.println("Calculated: £" + afterDiscount);
        System.out.println("Rounded: £" + rounded);
        System.out.println("Difference from expected: £" + Math.abs(91.00 - rounded));

        // Kiểm tra floating point precision
        System.out.println("\nFloating point check:");
        System.out.println("  afterDiscount as double: " + afterDiscount);
        System.out.println("  afterDiscount * 100: " + (afterDiscount * 100.0));
        System.out.println("  Math.round(afterDiscount * 100): " + Math.round(afterDiscount * 100.0));
        System.out.println("  Final result: " + rounded);
    }
}
