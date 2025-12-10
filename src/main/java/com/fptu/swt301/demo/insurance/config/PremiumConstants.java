package com.fptu.swt301.demo.insurance.config;

/**
 * Constants cho Insurance Premium Calculator
 * Tách riêng để dễ bảo trì và thay đổi
 */
public final class PremiumConstants {

    private PremiumConstants() {
        // Utility class - prevent instantiation
    }

    /**
     * Base premium để tính toán (deprecated - dùng BASE_PREMIUM_RATE thay thế)
     * Được điều chỉnh từ £1000 xuống £128.712 để phù hợp với kết quả thực tế
     * 
     * @deprecated Sử dụng BASE_PREMIUM_RATE để tính base premium dựa trên Estimated
     *             Value
     */
    @Deprecated
    public static final double BASE_PREMIUM = 128.712;

    /**
     * Tỷ lệ để tính BASE_PREMIUM dựa trên Estimated Value
     * BASE_PREMIUM = Estimated Value * BASE_PREMIUM_RATE
     * 
     * Ví dụ: Nếu Estimated Value = 100 và BASE_PREMIUM_RATE = 0.4667
     * thì BASE_PREMIUM = 100 * 0.4667 = 46.67
     */
    public static final double BASE_PREMIUM_RATE = 0.4667;

    /**
     * Phí bổ sung cho Windscreen Repair
     */
    public static final double WINDSCREEN_CHARGE = 30.0;

    /**
     * Phí bổ sung cho High Mileage (> 5000)
     */
    public static final double HIGH_MILEAGE_CHARGE = 50.0;

    /**
     * Phí bổ sung cho Public Parking
     */
    public static final double PUBLIC_PARKING_CHARGE = 30.0;

    /**
     * Discount cho Zero Accidents (30%)
     */
    public static final double ZERO_ACCIDENT_DISCOUNT = 0.30;

    /**
     * Giá trị ước tính tối thiểu của xe
     */
    public static final int MINIMUM_ESTIMATE_VALUE = 100;

    /**
     * Ngưỡng mileage để tính phí cao (High Mileage)
     */
    public static final int HIGH_MILEAGE_THRESHOLD = 5000;

    /**
     * Số chữ số thập phân để làm tròn premium
     */
    public static final int PREMIUM_DECIMAL_PLACES = 2;
}
