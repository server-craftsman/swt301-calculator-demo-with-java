package com.fptu.swt301.demo.insurance.service;

import com.fptu.swt301.demo.insurance.config.PremiumConstants;
import com.fptu.swt301.demo.insurance.domain.model.PremiumCalculationRequest;
import com.fptu.swt301.demo.insurance.domain.valueobject.BreakdownCover;

/**
 * Service để tính toán Insurance Premium
 * Tách business logic ra service layer
 */
public class PremiumCalculationService {

    /**
     * Tính premium dựa trên request
     * 
     * @param request Premium calculation request
     * @return Calculated premium (rounded to 2 decimal places)
     */
    public double calculatePremium(PremiumCalculationRequest request) {
        double premium = PremiumConstants.BASE_PREMIUM;

        // Apply breakdown cover percentage
        BreakdownCover breakdownCover = request.getBreakdownCover();
        premium = premium * (1 + breakdownCover.getPercentageIncrease());

        // Apply windscreen repair charge
        if (request.isWindscreenRepair()) {
            premium += PremiumConstants.WINDSCREEN_CHARGE;
        }

        // Apply zero accident discount
        if (request.getNumberOfAccidents() == 0) {
            premium = premium * (1 - PremiumConstants.ZERO_ACCIDENT_DISCOUNT);
        }

        // Apply high mileage charge
        if (request.getTotalMileage() > PremiumConstants.HIGH_MILEAGE_THRESHOLD) {
            premium += PremiumConstants.HIGH_MILEAGE_CHARGE;
        }

        // Apply public parking charge
        if (isPublicParking(request.getParkingLocation())) {
            premium += PremiumConstants.PUBLIC_PARKING_CHARGE;
        }

        return roundToDecimalPlaces(premium, PremiumConstants.PREMIUM_DECIMAL_PLACES);
    }

    /**
     * Tính premium với các tham số riêng lẻ
     * Convenience method để dễ sử dụng
     */
    public double calculatePremium(
            String breakdownCover,
            String windscreenRepair,
            int numberOfAccidents,
            int totalMileage,
            double estimatedValue,
            String parkingLocation) {
        
        PremiumCalculationRequest request = PremiumCalculationRequest.builder()
                .breakdownCover(breakdownCover)
                .windscreenRepair(windscreenRepair)
                .numberOfAccidents(numberOfAccidents)
                .totalMileage(totalMileage)
                .estimatedValue(estimatedValue)
                .parkingLocation(parkingLocation)
                .build();

        return calculatePremium(request);
    }

    private boolean isPublicParking(String parkingLocation) {
        if (parkingLocation == null) {
            return false;
        }
        return "Public Place".equalsIgnoreCase(parkingLocation.trim())
                || "Public place".equalsIgnoreCase(parkingLocation.trim());
    }

    private double roundToDecimalPlaces(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }
}
