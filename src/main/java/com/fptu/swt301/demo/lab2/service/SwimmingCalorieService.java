package com.fptu.swt301.demo.lab2.service;

import com.fptu.swt301.demo.lab2.config.SwimmingConstants;
import com.fptu.swt301.demo.lab2.domain.model.CalorieCalculationRequest;

/**
 * Service để tính toán lượng calo tiêu thụ khi bơi
 * Sử dụng công thức MET (Metabolic Equivalent of Task) chuẩn quốc tế
 * 
 * Công thức:
 * - Calories per minute = (MET × bodyWeightKg × 3.5) / 200
 * - Total calories burned = Calories per minute × durationMin
 */
public class SwimmingCalorieService {

    /**
     * Tính lượng calo tiêu thụ dựa trên request
     * 
     * @param request Calorie calculation request
     * @return Total calories burned (rounded to 2 decimal places)
     */
    public double calculateCaloriesBurned(CalorieCalculationRequest request) {
        double metValue = request.getSwimmingStyle().getMetValue();
        double bodyWeightKg = request.getBodyWeightKg();
        double durationMin = request.getDurationMin();

        // Tính calories per minute: (MET × bodyWeightKg × 3.5) / 200
        double caloriesPerMinute = (metValue * bodyWeightKg * SwimmingConstants.MET_CONVERSION_FACTOR)
                / SwimmingConstants.MET_DENOMINATOR;

        // Tính total calories: caloriesPerMinute × durationMin
        double totalCalories = caloriesPerMinute * durationMin;

        // Làm tròn đến 2 chữ số thập phân
        return roundToDecimalPlaces(totalCalories, SwimmingConstants.DECIMAL_PLACES);
    }

    /**
     * Tính calories per minute (không nhân với duration)
     * 
     * @param request Calorie calculation request
     * @return Calories per minute (rounded to 2 decimal places)
     */
    public double calculateCaloriesPerMinute(CalorieCalculationRequest request) {
        double metValue = request.getSwimmingStyle().getMetValue();
        double bodyWeightKg = request.getBodyWeightKg();

        // Tính calories per minute: (MET × bodyWeightKg × 3.5) / 200
        double caloriesPerMinute = (metValue * bodyWeightKg * SwimmingConstants.MET_CONVERSION_FACTOR)
                / SwimmingConstants.MET_DENOMINATOR;

        // Làm tròn đến 2 chữ số thập phân
        return roundToDecimalPlaces(caloriesPerMinute, SwimmingConstants.DECIMAL_PLACES);
    }

    /**
     * Convenience method để tính calories với các tham số riêng lẻ
     */
    public double calculateCaloriesBurned(
            String swimmingStyleName,
            double durationMin,
            double bodyWeightKg) {
        
        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(swimmingStyleName)
                .durationMin(durationMin)
                .bodyWeightKg(bodyWeightKg)
                .build();

        return calculateCaloriesBurned(request);
    }

    /**
     * Làm tròn số đến số chữ số thập phân chỉ định
     */
    private double roundToDecimalPlaces(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }
}

