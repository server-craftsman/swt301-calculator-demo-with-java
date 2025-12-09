package com.fptu.swt301.demo.insurance.config;

/**
 * Constants cho Broker Profile
 * Tách riêng để dễ bảo trì và thay đổi
 */
public final class ProfileConstants {

    private ProfileConstants() {
        // Utility class - prevent instantiation
    }

    /**
     * Valid title values từ Guru99 Insurance form
     */
    public static final String[] VALID_TITLES = {
            "Mr", "Mrs", "Miss", "Ms", "Doctor", "Captain", "Duchess", "Duke",
            "Father", "General", "Lady", "Lord", "Lieutenant", "Lieutenant Colonel",
            "Major", "Master", "Professor", "Reverend", "Sir", "Squire", "Squadron Leader"
    };

    /**
     * Valid occupation values từ Guru99 Insurance form
     */
    public static final String[] VALID_OCCUPATIONS = {
            "Academic", "Actor", "Artist", "Doctor", "Librarian", "Student",
            "Accountant", "Architect", "Dentist", "Economists", "Writer", "Engineer",
            "Lawyer", "Nurse", "Pharmacist", "Physician", "Physiotherapist",
            "Psychologist", "Scientist", "Social worker", "Statistician", "Surgeon",
            "Teacher", "Math Professor", "Bank Examiner", "Museum Curator", "Casino Dealer"
    };

    /**
     * Valid license types
     */
    public static final String LICENSE_TYPE_FULL = "Full";
    public static final String LICENSE_TYPE_PROVISIONAL = "Provisional";

    /**
     * Valid Vietnamese phone prefixes
     */
    public static final String[] VN_PHONE_PREFIXES = { "03", "05", "07", "08", "09" };

    /**
     * Minimum age requirement
     */
    public static final int MINIMUM_AGE = 18;

    /**
     * Maximum license period (years)
     */
    public static final int MAX_LICENSE_PERIOD = 50;

    /**
     * Minimum license period (years)
     */
    public static final int MIN_LICENSE_PERIOD = 0;

    /**
     * Vietnam phone number length (10 digits)
     */
    public static final int VN_PHONE_LENGTH_10 = 10;

    /**
     * Vietnam phone number length with country code (11 digits)
     */
    public static final int VN_PHONE_LENGTH_11 = 11;

    /**
     * Vietnam country code
     */
    public static final String VN_COUNTRY_CODE = "84";
}
