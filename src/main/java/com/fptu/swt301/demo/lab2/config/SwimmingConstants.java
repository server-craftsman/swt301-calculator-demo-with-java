package com.fptu.swt301.demo.lab2.config;

/**
 * Constants cho Swimming Calorie Calculator
 */
public final class SwimmingConstants {

    private SwimmingConstants() {
        // Utility class - prevent instantiation
    }

    /**
     * Hệ số chuyển đổi trong công thức MET
     */
    public static final double MET_CONVERSION_FACTOR = 3.5;

    /**
     * Mẫu số trong công thức tính calories per minute
     */
    public static final double MET_DENOMINATOR = 200.0;

    /**
     * Số chữ số thập phân để làm tròn kết quả
     */
    public static final int DECIMAL_PLACES = 2;

    /**
     * Giá trị cân nặng tối thiểu (kg)
     */
    public static final double MIN_BODY_WEIGHT_KG = 0.1;

    /**
     * Giá trị cân nặng tối đa hợp lý (kg)
     * Giới hạn theo web production: tối đa 500 kg
     */
    public static final double MAX_BODY_WEIGHT_KG = 634.9999999999;

    /**
     * Thời gian tối thiểu (phút)
     * Cho phép giá trị rất nhỏ (giây):
     * 0.00000000000000000000000000000000000000000000000000000000007
     * Chỉ cần > 0 là hợp lệ
     */
    public static final double MIN_DURATION_MIN = 0.00000000000000000000000000000000000000000000000000000000007;

    /**
     * Thời gian tối đa hợp lý (phút)
     * Giới hạn theo web production: thường tối đa 1440 phút (24 giờ)
     */
    public static final double MAX_DURATION_MIN = 1440.0;
}
