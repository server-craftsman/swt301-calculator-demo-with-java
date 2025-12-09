package com.fptu.swt301.demo.insurance.domain.valueobject;

import java.util.Objects;

/**
 * Value Object cho Breakdown Cover
 * Immutable và có validation
 */
public final class BreakdownCover {

    public static final BreakdownCover NO_COVER = new BreakdownCover("No cover", 0.01);
    public static final BreakdownCover ROADSIDE = new BreakdownCover("Roadside", 0.02);
    public static final BreakdownCover AT_HOME = new BreakdownCover("At home", 0.03);
    public static final BreakdownCover EUROPEAN = new BreakdownCover("European", 0.04);

    private final String name;
    private final double percentageIncrease;

    private BreakdownCover(String name, double percentageIncrease) {
        this.name = name;
        this.percentageIncrease = percentageIncrease;
    }

    /**
     * Factory method để tạo BreakdownCover từ string
     * 
     * @param name Tên breakdown cover
     * @return BreakdownCover instance
     * @throws IllegalArgumentException nếu name không hợp lệ
     */
    public static BreakdownCover fromString(String name) {
        if (name == null) {
            return NO_COVER; // Default
        }

        String normalized = name.trim().toLowerCase();
        switch (normalized) {
            case "no cover":
                return NO_COVER;
            case "roadside":
                return ROADSIDE;
            case "at home":
                return AT_HOME;
            case "european":
                return EUROPEAN;
            default:
                return NO_COVER; // Default fallback
        }
    }

    public String getName() {
        return name;
    }

    public double getPercentageIncrease() {
        return percentageIncrease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BreakdownCover that = (BreakdownCover) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
