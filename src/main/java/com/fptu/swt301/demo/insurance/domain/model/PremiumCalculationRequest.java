package com.fptu.swt301.demo.insurance.domain.model;

import com.fptu.swt301.demo.insurance.domain.valueobject.BreakdownCover;
import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model cho Premium Calculation Request
 * Chứa tất cả thông tin cần thiết để tính premium
 */
public class PremiumCalculationRequest {

    private final BreakdownCover breakdownCover;
    private final boolean windscreenRepair;
    private final int numberOfAccidents;
    private final int totalMileage;
    private final double estimatedValue;
    private final String parkingLocation;

    private PremiumCalculationRequest(Builder builder) {
        this.breakdownCover = builder.breakdownCover;
        this.windscreenRepair = builder.windscreenRepair;
        this.numberOfAccidents = builder.numberOfAccidents;
        this.totalMileage = builder.totalMileage;
        this.estimatedValue = builder.estimatedValue;
        this.parkingLocation = builder.parkingLocation;
    }

    public static Builder builder() {
        return new Builder();
    }

    public BreakdownCover getBreakdownCover() {
        return breakdownCover;
    }

    public boolean isWindscreenRepair() {
        return windscreenRepair;
    }

    public int getNumberOfAccidents() {
        return numberOfAccidents;
    }

    public int getTotalMileage() {
        return totalMileage;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    /**
     * Validate request và throw ValidationException nếu có lỗi
     */
    public void validate() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (numberOfAccidents < 0) {
            errors.add("Number of accidents cannot be negative. Provided value: " + numberOfAccidents);
        }

        if (totalMileage < 0) {
            errors.add("Total mileage cannot be negative. Provided value: " + totalMileage);
        }

        if (estimatedValue < 0) {
            errors.add("Estimated value cannot be negative. Provided value: £" + estimatedValue);
        }

        if (estimatedValue < 100) {
            errors.add("Estimated value must be at least £100. Provided value: £" + estimatedValue);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    /**
     * Builder pattern cho PremiumCalculationRequest
     */
    public static class Builder {
        private BreakdownCover breakdownCover = BreakdownCover.NO_COVER;
        private boolean windscreenRepair = false;
        private int numberOfAccidents = 0;
        private int totalMileage = 0;
        private double estimatedValue = 0.0;
        private String parkingLocation = "";

        public Builder breakdownCover(String breakdownCover) {
            this.breakdownCover = BreakdownCover.fromString(breakdownCover);
            return this;
        }

        public Builder breakdownCover(BreakdownCover breakdownCover) {
            this.breakdownCover = breakdownCover != null ? breakdownCover : BreakdownCover.NO_COVER;
            return this;
        }

        public Builder windscreenRepair(boolean windscreenRepair) {
            this.windscreenRepair = windscreenRepair;
            return this;
        }

        public Builder windscreenRepair(String windscreenRepair) {
            this.windscreenRepair = "Yes".equalsIgnoreCase(windscreenRepair)
                    || "true".equalsIgnoreCase(windscreenRepair);
            return this;
        }

        public Builder numberOfAccidents(int numberOfAccidents) {
            this.numberOfAccidents = numberOfAccidents;
            return this;
        }

        public Builder totalMileage(int totalMileage) {
            this.totalMileage = totalMileage;
            return this;
        }

        public Builder estimatedValue(double estimatedValue) {
            this.estimatedValue = estimatedValue;
            return this;
        }

        public Builder parkingLocation(String parkingLocation) {
            this.parkingLocation = parkingLocation != null ? parkingLocation : "";
            return this;
        }

        public PremiumCalculationRequest build() throws ValidationException {
            PremiumCalculationRequest request = new PremiumCalculationRequest(this);
            request.validate();
            return request;
        }
    }
}
