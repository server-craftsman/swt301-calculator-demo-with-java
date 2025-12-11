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

        // Validation cho Body Weight
        // Web production chỉ kiểm tra > 0, không có minimum threshold cụ thể
        // Sử dụng epsilon để xử lý giá trị rất nhỏ (floating point precision)
        // Giá trị <= 0 hoặc quá nhỏ (gần như 0) sẽ bị reject
        double epsilon = 1e-50; // Epsilon rất nhỏ để xử lý floating point precision
        if (bodyWeightKg <= 0.0 || bodyWeightKg < epsilon) {
            errors.add(String.format("Body weight must be more than 0 kg. Provided value: %.60f kg.",
                    bodyWeightKg));
        }

        // Không validate maximum - web production không có giới hạn max
        // if (bodyWeightKg > SwimmingConstants.MAX_BODY_WEIGHT_KG) {
        // errors.add(String.format("Body weight cannot exceed %.0f kg (web production
        // limit). " +
        // "Provided value: %.10f kg. Please enter a value between %.2f and %.0f kg.",
        // SwimmingConstants.MAX_BODY_WEIGHT_KG, bodyWeightKg,
        // SwimmingConstants.MIN_BODY_WEIGHT_KG, SwimmingConstants.MAX_BODY_WEIGHT_KG));
        // }

        // Validation cho Duration
        if (durationMin < 0.0) {
            errors.add(String.format("Duration cannot be negative. Provided value: %.10f minutes. " +
                    "Please enter a positive duration.",
                    durationMin));
        }

        if (durationMin == 0.0) {
            errors.add("Duration must be greater than 0 minutes. Please enter a valid duration.");
        }

        // Validate minimum duration (cho phép giá trị rất nhỏ nhưng phải >=
        // MIN_DURATION_MIN)
        if (durationMin > 0.0 && durationMin < SwimmingConstants.MIN_DURATION_MIN) {
            errors.add(String.format(
                    "Duration is too small. Minimum duration is %.60f minutes. " +
                            "Provided value: %.60f minutes.",
                    SwimmingConstants.MIN_DURATION_MIN, durationMin));
        }

        // Không validate maximum - web production không có giới hạn max cho duration
        // if (durationMin > SwimmingConstants.MAX_DURATION_MIN) {
        // errors.add(String.format("Duration cannot exceed %.0f minutes (%.1f hours,
        // web production limit). " +
        // "Provided value: %.2f minutes. Please enter a duration between %.2f and %.0f
        // minutes.",
        // SwimmingConstants.MAX_DURATION_MIN, SwimmingConstants.MAX_DURATION_MIN /
        // 60.0,
        // durationMin, SwimmingConstants.MIN_DURATION_MIN,
        // SwimmingConstants.MAX_DURATION_MIN));
        // }

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
