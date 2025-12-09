package com.fptu.swt301.demo.insurance.domain.valueobject;

import com.fptu.swt301.demo.insurance.config.ProfileConstants;
import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Value Object cho License Information
 * Immutable và có validation
 */
public final class LicenseInfo {

    private final String type;
    private final int period; // years

    private LicenseInfo(String type, int period) {
        this.type = type;
        this.period = period;
    }

    /**
     * Factory method để tạo LicenseInfo
     * 
     * @param type   License type (Full or Provisional)
     * @param period License period in years
     * @return LicenseInfo instance
     * @throws ValidationException nếu invalid
     */
    public static LicenseInfo of(String type, int period) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (type == null || type.trim().isEmpty()) {
            errors.add("License type is required");
        } else {
            String typeTrimmed = type.trim();
            if (!ProfileConstants.LICENSE_TYPE_FULL.equalsIgnoreCase(typeTrimmed)
                    && !ProfileConstants.LICENSE_TYPE_PROVISIONAL.equalsIgnoreCase(typeTrimmed)) {
                errors.add("License type must be 'Full' or 'Provisional'");
            }
        }

        if (period < ProfileConstants.MIN_LICENSE_PERIOD) {
            errors.add("License period cannot be negative. Provided value: " + period);
        }
        if (period > ProfileConstants.MAX_LICENSE_PERIOD) {
            errors.add("License period cannot exceed " + ProfileConstants.MAX_LICENSE_PERIOD
                    + " years. Provided value: " + period);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return new LicenseInfo(type != null ? type.trim() : type, period);
    }

    public String getType() {
        return type;
    }

    public int getPeriod() {
        return period;
    }

    public boolean isFull() {
        return type != null && ProfileConstants.LICENSE_TYPE_FULL.equalsIgnoreCase(type);
    }

    public boolean isProvisional() {
        return type != null && ProfileConstants.LICENSE_TYPE_PROVISIONAL.equalsIgnoreCase(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LicenseInfo that = (LicenseInfo) o;
        return period == that.period && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, period);
    }

    @Override
    public String toString() {
        return String.format("%s (%d years)", type, period);
    }
}
