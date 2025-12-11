package com.fptu.swt301.demo.lab2.debug;

import com.fptu.swt301.demo.lab2.domain.model.CalorieCalculationRequest;
import com.fptu.swt301.demo.lab2.domain.valueobject.SwimmingStyle;
import com.fptu.swt301.demo.lab2.service.SwimmingCalorieService;
import com.fptu.swt301.demo.lab2.exception.ValidationException;

/**
 * Demo class để minh họa cách sử dụng Swimming Calorie Calculator
 */
public class SwimmingCalorieDemo {

    public static void main(String[] args) {
        SwimmingCalorieService service = new SwimmingCalorieService();

        System.out.println("========================================");
        System.out.println("SWIMMING CALORIE CALCULATOR DEMO");
        System.out.println("========================================\n");

        // Demo 1: Butterfly stroke - High intensity
        demoCalculation(service, "Butterfly", 30.0, 70.0, 
                "Butterfly stroke - 30 minutes - 70 kg person");

        // Demo 2: Crawl recreational - Medium intensity
        demoCalculation(service, "Crawl (recreational)", 45.0, 65.0,
                "Recreational crawl - 45 minutes - 65 kg person");

        // Demo 3: Water aerobics - Low intensity
        demoCalculation(service, "Water aerobics and calisthenics", 60.0, 60.0,
                "Water aerobics - 60 minutes - 60 kg person");

        // Demo 4: Treading water relaxed - Very low intensity
        demoCalculation(service, "Treading water (relaxed)", 20.0, 80.0,
                "Relaxed treading water - 20 minutes - 80 kg person");

        // Demo 5: Invalid input - should throw exception
        System.out.println("\n--- Demo 5: Invalid Input (Expected Exception) ---");
        try {
            service.calculateCaloriesBurned("Invalid Style", 30.0, 70.0);
            System.out.println("ERROR: Should have thrown exception!");
        } catch (Exception e) {
            System.out.println("✓ Exception caught as expected:");
            if (e instanceof ValidationException) {
                ValidationException ve = (ValidationException) e;
                System.out.println("  Error count: " + ve.getErrorCount());
                ve.getErrors().forEach(error -> System.out.println("  - " + error));
            } else {
                System.out.println("  " + e.getMessage());
            }
        }

        // Demo 6: Using Builder pattern
        System.out.println("\n--- Demo 6: Using Builder Pattern ---");
        try {
            CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                    .swimmingStyle(SwimmingStyle.BUTTERFLY)
                    .durationMin(30.0)
                    .bodyWeightKg(70.0)
                    .build();

            double calories = service.calculateCaloriesBurned(request);
            double caloriesPerMin = service.calculateCaloriesPerMinute(request);

            System.out.println("Swimming Style: " + request.getSwimmingStyle().getDisplayName());
            System.out.println("MET Value: " + request.getSwimmingStyle().getMetValue());
            System.out.println("Duration: " + request.getDurationMin() + " minutes");
            System.out.println("Body Weight: " + request.getBodyWeightKg() + " kg");
            System.out.println("Calories per minute: " + caloriesPerMin + " kcal/min");
            System.out.println("Total calories burned: " + calories + " kcal");
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        System.out.println("\n========================================");
        System.out.println("DEMO COMPLETED");
        System.out.println("========================================");
    }

    private static void demoCalculation(SwimmingCalorieService service, 
                                       String swimmingStyle, 
                                       double durationMin, 
                                       double bodyWeightKg,
                                       String description) {
        System.out.println("\n--- " + description + " ---");
        try {
            double totalCalories = service.calculateCaloriesBurned(
                    swimmingStyle, durationMin, bodyWeightKg);

            // Calculate calories per minute for display
            CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                    .swimmingStyle(swimmingStyle)
                    .durationMin(durationMin)
                    .bodyWeightKg(bodyWeightKg)
                    .build();
            double caloriesPerMin = service.calculateCaloriesPerMinute(request);

            SwimmingStyle style = request.getSwimmingStyle();
            System.out.println("Swimming Style: " + style.getDisplayName());
            System.out.println("MET Value: " + style.getMetValue());
            System.out.println("Duration: " + durationMin + " minutes");
            System.out.println("Body Weight: " + bodyWeightKg + " kg");
            System.out.println("Calories per minute: " + caloriesPerMin + " kcal/min");
            System.out.println("Total calories burned: " + totalCalories + " kcal");
        } catch (ValidationException e) {
            System.out.println("Validation error:");
            e.getErrors().forEach(error -> System.out.println("  - " + error));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

