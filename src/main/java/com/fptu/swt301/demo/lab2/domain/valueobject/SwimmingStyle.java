package com.fptu.swt301.demo.lab2.domain.valueobject;

import java.util.Arrays;
import java.util.Optional;

/**
 * Value Object cho Swimming Style với MET values
 * Dựa trên tiêu chuẩn quốc tế và web calculator
 */
public enum SwimmingStyle {
    
    // Backstroke
    BACKSTROKE_INTENSE("Backstroke (intense)", 9.5),
    BACKSTROKE_RECREATIONAL("Backstroke (recreational)", 4.8),
    
    // Breaststroke
    BREASTSTROKE_INTENSE("Breaststroke (intense)", 10.3),
    BREASTSTROKE_RECREATIONAL("Breaststroke (recreational)", 5.3),
    
    // Butterfly
    BUTTERFLY("Butterfly", 13.8),
    
    // Crawl (Freestyle)
    CRAWL_INTENSE("Crawl (intense)", 10.0),
    CRAWL_RECREATIONAL("Crawl (recreational)", 8.3),
    
    // Sidestroke
    SIDESTROKE("Sidestroke", 7.0),
    
    // Treading water
    TREADING_WATER_HIGH_EFFORT("Treading water (high effort)", 9.8),
    TREADING_WATER_RELAXED("Treading water (relaxed)", 3.5),
    
    // Water aerobics
    WATER_AEROBICS("Water aerobics and calisthenics", 5.5),
    
    // Aqua jogging
    AQUA_JOGGING("Aqua jogging", 9.8),
    
    // Water walking
    WATER_WALKING_HIGH_EFFORT("Water walking (high effort)", 6.8),
    WATER_WALKING_RELAXED("Water walking (relaxed)", 4.5);

    private final String displayName;
    private final double metValue;

    SwimmingStyle(String displayName, double metValue) {
        this.displayName = displayName;
        this.metValue = metValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getMetValue() {
        return metValue;
    }

    /**
     * Tìm SwimmingStyle từ display name (case-insensitive, flexible matching)
     */
    public static Optional<SwimmingStyle> fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalized = name.trim();

        // Exact match first
        return Arrays.stream(values())
                .filter(style -> style.displayName.equalsIgnoreCase(normalized))
                .findFirst()
                .or(() -> {
                    // Flexible matching - try partial matches
                    return Arrays.stream(values())
                            .filter(style -> style.displayName.toLowerCase().contains(normalized.toLowerCase())
                                    || normalized.toLowerCase().contains(style.displayName.toLowerCase()))
                            .findFirst();
                });
    }

    /**
     * Kiểm tra xem string có phải là swimming style hợp lệ không
     */
    public static boolean isValid(String name) {
        return fromString(name).isPresent();
    }
}

