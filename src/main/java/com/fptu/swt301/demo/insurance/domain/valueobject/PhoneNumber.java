package com.fptu.swt301.demo.insurance.domain.valueobject;

import com.fptu.swt301.demo.insurance.config.ProfileConstants;
import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Value Object cho Phone Number (Vietnam format)
 * Immutable và có validation
 */
public final class PhoneNumber {

    private final String value;
    private final String digitsOnly;

    private PhoneNumber(String value) {
        this.value = value;
        this.digitsOnly = extractDigits(value);
    }

    /**
     * Factory method để tạo PhoneNumber từ string
     * 
     * @param phoneNumber Số điện thoại
     * @return PhoneNumber instance
     * @throws ValidationException nếu phone number không hợp lệ
     */
    public static PhoneNumber of(String phoneNumber) throws ValidationException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new ValidationException("Phone number is required");
        }

        List<String> errors = validatePhoneNumber(phoneNumber);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return new PhoneNumber(phoneNumber.trim());
    }

    private static List<String> validatePhoneNumber(String phone) {
        List<String> errors = new ArrayList<>();
        String phoneTrimmed = phone.trim();

        // Reject negative numbers
        if (phoneTrimmed.startsWith("-")) {
            errors.add("Phone number cannot be negative (cannot start with minus sign)");
            return errors; // Early return - no need to check further
        }

        // Only allow digits, +, and spaces
        String phoneWithoutValidChars = phone.replaceAll("[0-9+\\s]", "");
        if (!phoneWithoutValidChars.isEmpty()) {
            errors.add(
                    "Phone number can only contain digits, + (for country code), and spaces. Invalid characters found: "
                            + phoneWithoutValidChars);
            return errors; // Early return
        }

        // Extract only digits
        String phoneDigits = phone.replaceAll("[^0-9]", "");

        // Validate length
        if (phoneDigits.length() != ProfileConstants.VN_PHONE_LENGTH_10
                && phoneDigits.length() != ProfileConstants.VN_PHONE_LENGTH_11) {
            errors.add("Phone number must be 10 or 11 digits (Vietnam format: 0xxxxxxxxx or +84xxxxxxxxx)");
            return errors; // Early return
        }

        // Validate 10-digit format
        if (phoneDigits.length() == ProfileConstants.VN_PHONE_LENGTH_10) {
            if (!phoneDigits.startsWith("0")) {
                errors.add("10-digit phone number must start with '0' (Vietnam format)");
            } else {
                String prefix = phoneDigits.substring(0, 2);
                Set<String> validPrefixes = Arrays.stream(ProfileConstants.VN_PHONE_PREFIXES)
                        .collect(Collectors.toSet());
                if (!validPrefixes.contains(prefix)) {
                    errors.add("Phone number prefix must be one of: "
                            + Arrays.toString(ProfileConstants.VN_PHONE_PREFIXES));
                }
            }
        }

        // Validate 11-digit format
        if (phoneDigits.length() == ProfileConstants.VN_PHONE_LENGTH_11) {
            if (!phoneDigits.startsWith(ProfileConstants.VN_COUNTRY_CODE)) {
                errors.add("11-digit phone number (with country code) must start with '84'");
            } else {
                String prefix = phoneDigits.substring(2, 4);
                Set<String> validPrefixes = Arrays.stream(ProfileConstants.VN_PHONE_PREFIXES)
                        .collect(Collectors.toSet());
                if (!validPrefixes.contains("0" + prefix)) {
                    errors.add("Phone number prefix (after +84) must be one of: "
                            + Arrays.toString(ProfileConstants.VN_PHONE_PREFIXES));
                }
            }
        }

        return errors;
    }

    private static String extractDigits(String phone) {
        return phone == null ? "" : phone.replaceAll("[^0-9]", "");
    }

    public String getValue() {
        return value;
    }

    public String getDigitsOnly() {
        return digitsOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(digitsOnly, that.digitsOnly);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digitsOnly);
    }

    @Override
    public String toString() {
        return value;
    }
}
