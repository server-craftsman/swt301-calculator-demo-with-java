package com.fptu.swt301.demo.lab2.domain.model;

import com.fptu.swt301.demo.lab2.config.SwimmingConstants;
import com.fptu.swt301.demo.lab2.domain.valueobject.SwimmingStyle;
import com.fptu.swt301.demo.lab2.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain model cho Calorie Calculation Request
 * Chứa thông tin cần thiết để tính toán lượng calo tiêu thụ khi bơi
 */
public class CalorieCalculationRequest {

    private final SwimmingStyle swimmingStyle;
    private final double durationMin;
    private final double bodyWeightKg;

    private CalorieCalculationRequest(Builder builder) {
        this.swimmingStyle = builder.swimmingStyle;
        this.durationMin = builder.durationMin;
        this.bodyWeightKg = builder.bodyWeightKg;
    }

    public static Builder builder() {
        return new Builder();
    }

    public SwimmingStyle getSwimmingStyle() {
        return swimmingStyle;
    }

    public double getDurationMin() {
        return durationMin;
    }

    public double getBodyWeightKg() {
        return bodyWeightKg;
    }

    /**
     * Validate request và throw ValidationException nếu có lỗi
     */
    public void validate() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (swimmingStyle == null) {
            errors.add("Swimming style cannot be null or invalid. Please select a valid swimming style.");
        }

        if (bodyWeightKg < SwimmingConstants.MIN_BODY_WEIGHT_KG) {
            errors.add(String.format("Body weight cannot be less than %.1f kg. Provided value: %.2f kg",
                    SwimmingConstants.MIN_BODY_WEIGHT_KG, bodyWeightKg));
        }

        if (bodyWeightKg > SwimmingConstants.MAX_BODY_WEIGHT_KG) {
            errors.add(String.format("Body weight cannot exceed %.1f kg. Provided value: %.2f kg",
                    SwimmingConstants.MAX_BODY_WEIGHT_KG, bodyWeightKg));
        }

        if (durationMin < SwimmingConstants.MIN_DURATION_MIN) {
            errors.add(String.format("Duration cannot be negative. Provided value: %.2f minutes", durationMin));
        }

        if (durationMin > SwimmingConstants.MAX_DURATION_MIN) {
            errors.add(String.format("Duration cannot exceed %.1f minutes. Provided value: %.2f minutes",
                    SwimmingConstants.MAX_DURATION_MIN, durationMin));
        }

        if (durationMin == 0.0) {
            errors.add("Duration must be greater than 0 minutes");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public static class Builder {
        private SwimmingStyle swimmingStyle;
        private double durationMin = 0.0;
        private double bodyWeightKg = 0.0;

        public Builder swimmingStyle(SwimmingStyle swimmingStyle) {
            this.swimmingStyle = swimmingStyle;
            return this;
        }

        public Builder swimmingStyle(String swimmingStyleName) {
            this.swimmingStyle = SwimmingStyle.fromString(swimmingStyleName)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Invalid swimming style: " + swimmingStyleName));
            return this;
        }

        public Builder durationMin(double durationMin) {
            this.durationMin = durationMin;
            return this;
        }

        public Builder bodyWeightKg(double bodyWeightKg) {
            this.bodyWeightKg = bodyWeightKg;
            return this;
        }

        public CalorieCalculationRequest build() {
            CalorieCalculationRequest request = new CalorieCalculationRequest(this);
            request.validate();
            return request;
        }
    }
}

