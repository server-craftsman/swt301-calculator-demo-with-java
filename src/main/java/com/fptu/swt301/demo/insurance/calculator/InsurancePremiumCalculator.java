package com.fptu.swt301.demo.insurance.calculator;

import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Insurance Premium Calculator theo yêu cầu SRS Guru99 Insurance
 * 
 * Technical Requirements:
 * T1: No Cover - Increase by 1%
 * T2: Roadside - Increase by 2%
 * T3: At home - Increase by 3%
 * T4: European - Increase by 4%
 * T5: If Windscreen Repair selected - Increment premium by £30
 * T6: If Number Of Accidents Last Year is zero, 30% discount on premium
 * T7: If Total Mileage >5000 - Increment premium by £50
 * T8: If Estimate Value <100 - Do not proceed
 * T9: If Parking Location is Public Place - Increment premium by £30
 */
public class InsurancePremiumCalculator {

    // Base premium để tính toán
    private static final double BASE_PREMIUM = 128.712;
    private static final double WINDSCREEN_CHARGE = 30.0;
    private static final double HIGH_MILEAGE_CHARGE = 50.0;
    private static final double PUBLIC_PARKING_CHARGE = 30.0;
    private static final double ZERO_ACCIDENT_DISCOUNT = 0.30; // 30%
    private static final int MINIMUM_ESTIMATE_VALUE = 100;
    private static final int HIGH_MILEAGE_THRESHOLD = 5000;

    /**
     * Tính premium dựa trên các thông số đầu vào
     * 
     * @param breakdownCover    Loại bảo hiểm hỗ trợ: No cover, Roadside, At home,
     *                          European
     * @param windscreenRepair  Có sửa kính chắn gió không: Yes/No
     * @param numberOfAccidents Số tai nạn năm ngoái
     * @param totalMileage      Tổng số dặm đi trong năm
     * @param estimatedValue    Giá trị ước tính của xe
     * @param parkingLocation   Vị trí đỗ xe: Driveway/Carport, Locked Garage,
     *                          Public Place,
     *                          Private Property, Street/Road, Unlocked Garage
     * @return Premium tính được
     * @throws IllegalArgumentException nếu estimatedValue < 100
     */
    public double calculatePremium(
            String breakdownCover,
            String windscreenRepair,
            int numberOfAccidents,
            int totalMileage,
            double estimatedValue,
            String parkingLocation) {

        // Collect all validation errors
        List<String> errors = new ArrayList<>();

        // Validation: Number of Accidents cannot be negative
        if (numberOfAccidents < 0) {
            errors.add("Number of accidents cannot be negative. Provided value: " + numberOfAccidents);
        }

        // Validation: Total Mileage cannot be negative
        if (totalMileage < 0) {
            errors.add("Total mileage cannot be negative. Provided value: " + totalMileage);
        }

        // Validation: Estimated Value cannot be negative
        if (estimatedValue < 0) {
            errors.add("Estimated value cannot be negative. Provided value: £" + estimatedValue);
        }

        // T8: If Estimate Value <100 - Do not proceed
        if (estimatedValue < MINIMUM_ESTIMATE_VALUE) {
            errors.add("Estimated value must be at least £" + MINIMUM_ESTIMATE_VALUE
                    + ". Provided value: £" + estimatedValue);
        }

        // Throw ValidationException with all errors if any
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Bắt đầu với base premium
        double premium = BASE_PREMIUM;

        // Tính phần trăm tăng dựa trên breakdown cover (T1-T4)
        double breakdownPercentage = getBreakdownCoverPercentage(breakdownCover);
        premium = premium * (1 + breakdownPercentage);

        // T5: If Windscreen Repair selected - Increment premium by £30
        if ("Yes".equalsIgnoreCase(windscreenRepair) || "true".equalsIgnoreCase(windscreenRepair)) {
            premium += WINDSCREEN_CHARGE;
        }

        // T6: If Number Of Accidents Last Year is zero, 30% discount on premium
        if (numberOfAccidents == 0) {
            premium = premium * (1 - ZERO_ACCIDENT_DISCOUNT);
        }

        // T7: If Total Mileage >5000 - Increment premium by £50
        if (totalMileage > HIGH_MILEAGE_THRESHOLD) {
            premium += HIGH_MILEAGE_CHARGE;
        }

        // T9: If Parking Location is Public Place - Increment premium by £30
        if ("Public Place".equalsIgnoreCase(parkingLocation) ||
                "Public place".equalsIgnoreCase(parkingLocation)) {
            premium += PUBLIC_PARKING_CHARGE;
        }

        // Làm tròn đến 2 chữ số thập phân
        // Sử dụng Math.round với epsilon nhỏ để xử lý floating point precision
        return Math.round(premium * 100.0) / 100.0;
    }

    /**
     * Lấy phần trăm tăng dựa trên loại breakdown cover
     */
    private double getBreakdownCoverPercentage(String breakdownCover) {
        if (breakdownCover == null) {
            return 0.0;
        }

        switch (breakdownCover.toLowerCase().trim()) {
            case "no cover":
                return 0.01; // T1: 1%
            case "roadside":
                return 0.02; // T2: 2%
            case "at home":
                return 0.03; // T3: 3%
            case "european":
                return 0.04; // T4: 4%
            default:
                return 0.0;
        }
    }

    /**
     * Tính premium với các tham số bổ sung
     * Overload method để hỗ trợ nhiều trường hợp sử dụng khác nhau
     */
    public double calculatePremium(InsuranceQuoteRequest request) {
        return calculatePremium(
                request.getBreakdownCover(),
                request.getWindscreenRepair(),
                request.getNumberOfAccidents(),
                request.getTotalMileage(),
                request.getEstimatedValue(),
                request.getParkingLocation());
    }
}
