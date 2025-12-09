package com.fptu.swt301.demo.insurance.profile;

import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class đại diện cho Profile của Insurance Broker
 * Theo SRS Guru99 Insurance - Edit Profile module
 */
public class BrokerProfile {
    private String userId;
    private String title; // Valid values: Mr, Mrs, Miss, Ms, Doctor, Captain, Duchess, Duke, Father,
                          // General, Lady, Lord, Lieutenant, Lieutenant Colonel, Major, Master,
                          // Professor, Reverend, Sir, Squire, Squadron Leader
    private String firstName;
    private String surname;
    private String phone;
    private LocalDate dateOfBirth;
    private String licenseType; // Full, Provisional
    private int licensePeriod; // years
    private String occupation; // Valid values: Academic, Actor, Artist, Doctor, Librarian, Student,
                               // Accountant, Architect, Dentist, Economists, Writer, Engineer, Lawyer, Nurse,
                               // Pharmacist, Physician, Physiotherapist, Psychologist, Scientist, Social
                               // worker, Statistician, Surgeon, Teacher, Math Professor, Bank Examiner, Museum
                               // Curator, Casino Dealer

    // Address
    private String street;
    private String city;
    private String county;
    private String postCode;

    // Driver History (optional)
    private String driverHistory;

    public BrokerProfile() {
    }

    public BrokerProfile(String userId, String title, String firstName, String surname) {
        this.userId = userId;
        this.title = title;
        this.firstName = firstName;
        this.surname = surname;
    }

    // Valid title values from Guru99 Insurance form
    private static final String[] VALID_TITLES = {
            "Mr", "Mrs", "Miss", "Ms", "Doctor", "Captain", "Duchess", "Duke",
            "Father", "General", "Lady", "Lord", "Lieutenant", "Lieutenant Colonel",
            "Major", "Master", "Professor", "Reverend", "Sir", "Squire", "Squadron Leader"
    };

    // Valid occupation values from Guru99 Insurance form
    private static final String[] VALID_OCCUPATIONS = {
            "Academic", "Actor", "Artist", "Doctor", "Librarian", "Student",
            "Accountant", "Architect", "Dentist", "Economists", "Writer", "Engineer",
            "Lawyer", "Nurse", "Pharmacist", "Physician", "Physiotherapist",
            "Psychologist", "Scientist", "Social worker", "Statistician", "Surgeon",
            "Teacher", "Math Professor", "Bank Examiner", "Museum Curator", "Casino Dealer"
    };

    /**
     * Validate profile data
     * 
     * @return true nếu valid, false nếu invalid
     */
    public boolean isValid() {
        // UserId validation: required and not empty
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        // Title validation: required and must be one of the valid titles
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        boolean validTitle = false;
        for (String validTitleValue : VALID_TITLES) {
            if (validTitleValue.equalsIgnoreCase(title.trim())) {
                validTitle = true;
                break;
            }
        }
        if (!validTitle) {
            return false;
        }

        // FirstName validation: required and not empty
        if (firstName == null || firstName.trim().isEmpty()) {
            return false;
        }

        // Surname validation: required and not empty
        if (surname == null || surname.trim().isEmpty()) {
            return false;
        }

        // Phone validation: required and not empty
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        // DateOfBirth validation: required
        if (dateOfBirth == null) {
            return false;
        }

        // DateOfBirth validation: cannot be in the future
        LocalDate now = LocalDate.now();
        if (dateOfBirth.isAfter(now)) {
            return false;
        }

        // Occupation validation: required and must be one of the valid occupations
        if (occupation == null || occupation.trim().isEmpty()) {
            return false;
        }
        boolean validOccupation = false;
        for (String validOccupationValue : VALID_OCCUPATIONS) {
            if (validOccupationValue.equalsIgnoreCase(occupation.trim())) {
                validOccupation = true;
                break;
            }
        }
        if (!validOccupation) {
            return false;
        }

        // LicenseType validation: required and must be "Full" or "Provisional"
        if (licenseType == null || licenseType.trim().isEmpty()) {
            return false;
        }
        String licenseTypeTrimmed = licenseType.trim();
        if (!"Full".equalsIgnoreCase(licenseTypeTrimmed)
                && !"Provisional".equalsIgnoreCase(licenseTypeTrimmed)) {
            return false;
        }

        // LicensePeriod validation: must be >= 0 and <= 50 (reasonable maximum)
        if (licensePeriod < 0 || licensePeriod > 50) {
            return false;
        }

        // Phone validation: Vietnam phone format
        // Format: 10 digits starting with 0 (e.g., 0912345678)
        // Or 11 digits with country code +84 (e.g., +84912345678)
        // Only allow digits, + (for country code), and spaces
        // Reject negative numbers and special characters

        String phoneTrimmed = phone.trim();

        // Reject if starts with minus sign (negative number)
        if (phoneTrimmed.startsWith("-")) {
            return false;
        }

        // Only allow digits, +, and spaces - reject any other characters
        // Check if phone contains invalid characters (anything except digits, +,
        // spaces)
        String phoneWithoutValidChars = phone.replaceAll("[0-9+\\s]", "");
        if (!phoneWithoutValidChars.isEmpty()) {
            return false; // Contains invalid characters like ^, @, -, etc.
        }

        // Extract only digits for validation (remove +, spaces, etc.)
        String phoneDigits = phone.replaceAll("[^0-9]", "");

        if (phoneDigits.length() == 10) {
            // Must start with 0 and valid prefix (03, 05, 07, 08, 09)
            if (!phoneDigits.startsWith("0")) {
                return false;
            }
            String prefix = phoneDigits.substring(0, 2);
            if (!prefix.equals("03") && !prefix.equals("05") && !prefix.equals("07")
                    && !prefix.equals("08") && !prefix.equals("09")) {
                return false;
            }
        } else if (phoneDigits.length() == 11) {
            // Must start with 84 (country code) and valid prefix
            // Format: +84xxxxxxxxx or 84xxxxxxxxx
            if (!phoneDigits.startsWith("84")) {
                return false;
            }
            String prefix = phoneDigits.substring(2, 4);
            if (!prefix.equals("03") && !prefix.equals("05") && !prefix.equals("07")
                    && !prefix.equals("08") && !prefix.equals("09")) {
                return false;
            }
        } else {
            return false;
        }

        // Age validation: must be at least 18 years old (calculate accurately
        // considering month and day)
        int age = now.getYear() - dateOfBirth.getYear();
        // Adjust age if birthday hasn't occurred this year
        if (now.getMonthValue() < dateOfBirth.getMonthValue()
                || (now.getMonthValue() == dateOfBirth.getMonthValue()
                        && now.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            age--;
        }
        if (age < 18) {
            return false;
        }

        // Address validation: if provided, must not be only whitespace
        if (street != null && street.trim().isEmpty()) {
            return false;
        }
        if (city != null && city.trim().isEmpty()) {
            return false;
        }
        if (county != null && county.trim().isEmpty()) {
            return false;
        }
        if (postCode != null && postCode.trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Check if title is valid
     */
    public static boolean isValidTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        for (String validTitle : VALID_TITLES) {
            if (validTitle.equalsIgnoreCase(title.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if occupation is valid
     */
    public static boolean isValidOccupation(String occupation) {
        if (occupation == null || occupation.trim().isEmpty()) {
            return false;
        }
        for (String validOccupation : VALID_OCCUPATIONS) {
            if (validOccupation.equalsIgnoreCase(occupation.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate profile and collect all errors
     * 
     * @throws ValidationException containing all validation errors
     */
    public void validate() throws ValidationException {
        List<String> errors = new ArrayList<>();

        // UserId validation
        if (userId == null || userId.trim().isEmpty()) {
            errors.add("User ID is required and cannot be empty");
        }

        // Title validation
        if (title == null || title.trim().isEmpty()) {
            errors.add("Title is required");
        } else if (!isValidTitle(title)) {
            errors.add("Title must be one of: " + String.join(", ", VALID_TITLES));
        }

        // FirstName validation
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First name is required and cannot be empty");
        }

        // Surname validation
        if (surname == null || surname.trim().isEmpty()) {
            errors.add("Surname is required and cannot be empty");
        }

        // Phone validation
        if (phone == null || phone.trim().isEmpty()) {
            errors.add("Phone number is required");
        } else {
            String phoneTrimmed = phone.trim();
            if (phoneTrimmed.startsWith("-")) {
                errors.add("Phone number cannot be negative (cannot start with minus sign)");
            }
            String phoneWithoutValidChars = phone.replaceAll("[0-9+\\s]", "");
            if (!phoneWithoutValidChars.isEmpty()) {
                errors.add(
                        "Phone number can only contain digits, + (for country code), and spaces. Invalid characters found: "
                                + phoneWithoutValidChars);
            }
            String phoneDigits = phone.replaceAll("[^0-9]", "");
            if (phoneDigits.length() != 10 && phoneDigits.length() != 11) {
                errors.add("Phone number must be 10 or 11 digits (Vietnam format: 0xxxxxxxxx or +84xxxxxxxxx)");
            } else if (phoneDigits.length() == 10 && !phoneDigits.startsWith("0")) {
                errors.add("Phone number must start with 0 (Vietnam format)");
            } else if (phoneDigits.length() == 10) {
                String prefix = phoneDigits.substring(0, 2);
                if (!prefix.equals("03") && !prefix.equals("05") && !prefix.equals("07")
                        && !prefix.equals("08") && !prefix.equals("09")) {
                    errors.add("Phone number prefix must be 03, 05, 07, 08, or 09");
                }
            } else if (phoneDigits.length() == 11 && !phoneDigits.startsWith("84")) {
                errors.add("Phone number with country code must start with 84");
            } else if (phoneDigits.length() == 11) {
                String prefix = phoneDigits.substring(2, 4);
                if (!prefix.equals("03") && !prefix.equals("05") && !prefix.equals("07")
                        && !prefix.equals("08") && !prefix.equals("09")) {
                    errors.add("Phone number prefix must be 03, 05, 07, 08, or 09");
                }
            }
        }

        // DateOfBirth validation
        if (dateOfBirth == null) {
            errors.add("Date of birth is required");
        } else {
            LocalDate now = LocalDate.now();
            if (dateOfBirth.isAfter(now)) {
                errors.add("Date of birth cannot be in the future");
            } else {
                int age = now.getYear() - dateOfBirth.getYear();
                if (now.getMonthValue() < dateOfBirth.getMonthValue()
                        || (now.getMonthValue() == dateOfBirth.getMonthValue()
                                && now.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
                    age--;
                }
                if (age < 18) {
                    errors.add("Age must be at least 18 years old");
                }
            }
        }

        // LicenseType validation
        if (licenseType == null || licenseType.trim().isEmpty()) {
            errors.add("License type is required");
        } else {
            String licenseTypeTrimmed = licenseType.trim();
            if (!"Full".equalsIgnoreCase(licenseTypeTrimmed)
                    && !"Provisional".equalsIgnoreCase(licenseTypeTrimmed)) {
                errors.add("License type must be 'Full' or 'Provisional'");
            }
        }

        // LicensePeriod validation
        if (licensePeriod < 0) {
            errors.add("License period cannot be negative. Provided value: " + licensePeriod);
        }
        if (licensePeriod > 50) {
            errors.add("License period cannot exceed 50 years. Provided value: " + licensePeriod);
        }

        // Occupation validation
        if (occupation == null || occupation.trim().isEmpty()) {
            errors.add("Occupation is required");
        } else if (!isValidOccupation(occupation)) {
            errors.add("Occupation must be one of: " + String.join(", ", VALID_OCCUPATIONS));
        }

        // Address validation
        if (street != null && street.trim().isEmpty()) {
            errors.add("Street address cannot be only whitespace");
        }
        if (city != null && city.trim().isEmpty()) {
            errors.add("City cannot be only whitespace");
        }
        if (county != null && county.trim().isEmpty()) {
            errors.add("County cannot be only whitespace");
        }
        if (postCode != null && postCode.trim().isEmpty()) {
            errors.add("Post code cannot be only whitespace");
        }

        // Throw exception if there are any errors
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    /**
     * Get detailed validation error message (for backward compatibility)
     * 
     * @return Error message describing which validation failed, or null if valid
     * @deprecated Use validate() method instead to get all errors at once
     */
    @Deprecated
    public String getValidationErrorMessage() {
        if (userId == null || userId.trim().isEmpty()) {
            return "User ID is required and cannot be empty";
        }
        if (title == null || title.trim().isEmpty()) {
            return "Title is required";
        }
        if (!isValidTitle(title)) {
            return "Title must be one of: " + String.join(", ", VALID_TITLES);
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            return "First name is required and cannot be empty";
        }
        if (surname == null || surname.trim().isEmpty()) {
            return "Surname is required and cannot be empty";
        }
        if (phone == null || phone.trim().isEmpty()) {
            return "Phone number is required";
        }

        String phoneTrimmed = phone.trim();

        // Reject negative numbers
        if (phoneTrimmed.startsWith("-")) {
            return "Phone number cannot be negative (cannot start with minus sign)";
        }

        // Only allow digits, + (for country code), and spaces
        // Reject any other special characters (^, @, -, etc.)
        String phoneWithoutValidChars = phone.replaceAll("[0-9+\\s]", "");
        if (!phoneWithoutValidChars.isEmpty()) {
            return "Phone number can only contain digits, + (for country code), and spaces. Invalid characters found: "
                    + phoneWithoutValidChars;
        }

        String phoneDigits = phone.replaceAll("[^0-9]", "");
        if (phoneDigits.length() != 10 && phoneDigits.length() != 11) {
            return "Phone number must be 10 or 11 digits (Vietnam format: 0xxxxxxxxx or +84xxxxxxxxx)";
        }
        if (phoneDigits.length() == 10 && !phoneDigits.startsWith("0")) {
            return "Phone number must start with 0 (Vietnam format)";
        }
        if (phoneDigits.length() == 10) {
            String prefix = phoneDigits.substring(0, 2);
            if (!prefix.equals("03") && !prefix.equals("05") && !prefix.equals("07")
                    && !prefix.equals("08") && !prefix.equals("09")) {
                return "Phone number prefix must be 03, 05, 07, 08, or 09";
            }
        }
        if (phoneDigits.length() == 11 && !phoneDigits.startsWith("84")) {
            return "Phone number with country code must start with 84";
        }
        if (phoneDigits.length() == 11) {
            String prefix = phoneDigits.substring(2, 4);
            if (!prefix.equals("03") && !prefix.equals("05") && !prefix.equals("07")
                    && !prefix.equals("08") && !prefix.equals("09")) {
                return "Phone number prefix must be 03, 05, 07, 08, or 09";
            }
        }
        if (dateOfBirth == null) {
            return "Date of birth is required";
        }
        LocalDate now = LocalDate.now();
        if (dateOfBirth.isAfter(now)) {
            return "Date of birth cannot be in the future";
        }
        int age = now.getYear() - dateOfBirth.getYear();
        if (now.getMonthValue() < dateOfBirth.getMonthValue()
                || (now.getMonthValue() == dateOfBirth.getMonthValue()
                        && now.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            age--;
        }
        if (age < 18) {
            return "Age must be at least 18 years old";
        }
        if (licenseType == null || licenseType.trim().isEmpty()) {
            return "License type is required";
        }
        String licenseTypeTrimmed = licenseType.trim();
        if (!"Full".equalsIgnoreCase(licenseTypeTrimmed)
                && !"Provisional".equalsIgnoreCase(licenseTypeTrimmed)) {
            return "License type must be 'Full' or 'Provisional'";
        }
        if (licensePeriod < 0) {
            return "License period cannot be negative";
        }
        if (licensePeriod > 50) {
            return "License period cannot exceed 50 years";
        }
        if (occupation == null || occupation.trim().isEmpty()) {
            return "Occupation is required";
        }
        if (!isValidOccupation(occupation)) {
            return "Occupation must be one of: " + String.join(", ", VALID_OCCUPATIONS);
        }
        if (street != null && street.trim().isEmpty()) {
            return "Street address cannot be only whitespace";
        }
        if (city != null && city.trim().isEmpty()) {
            return "City cannot be only whitespace";
        }
        if (county != null && county.trim().isEmpty()) {
            return "County cannot be only whitespace";
        }
        if (postCode != null && postCode.trim().isEmpty()) {
            return "Post code cannot be only whitespace";
        }
        return null; // Valid
    }

    /**
     * Print profile details
     */
    public void printProfile() {
        System.out.println("========================================");
        System.out.println("BROKER PROFILE");
        System.out.println("========================================");
        System.out.println("Title: " + (title != null ? title : ""));
        System.out.println("Firstname: " + (firstName != null ? firstName : ""));
        System.out.println("Surname: " + (surname != null ? surname : ""));
        System.out.println("Phone: " + (phone != null ? phone : ""));
        System.out.println("Date of Birth: " + (dateOfBirth != null ? dateOfBirth : ""));
        System.out.println("License type: " + (licenseType != null ? licenseType : ""));
        System.out.println("License period: " + licensePeriod + " years");
        System.out.println("Occupation: " + (occupation != null ? occupation : ""));
        System.out.println("\nADDRESS:");
        System.out.println("  Street: " + (street != null ? street : ""));
        System.out.println("  City: " + (city != null ? city : ""));
        System.out.println("  County: " + (county != null ? county : ""));
        System.out.println("  Post code: " + (postCode != null ? postCode : ""));

        if (driverHistory != null && !driverHistory.isEmpty()) {
            System.out.println("\nDriver History: " + driverHistory);
        }
        System.out.println("========================================\n");
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public int getLicensePeriod() {
        return licensePeriod;
    }

    public void setLicensePeriod(int licensePeriod) {
        this.licensePeriod = licensePeriod;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getDriverHistory() {
        return driverHistory;
    }

    public void setDriverHistory(String driverHistory) {
        this.driverHistory = driverHistory;
    }

    @Override
    public String toString() {
        return "BrokerProfile{" +
                "userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", licenseType='" + licenseType + '\'' +
                ", licensePeriod=" + licensePeriod +
                ", occupation='" + occupation + '\'' +
                '}';
    }
}
