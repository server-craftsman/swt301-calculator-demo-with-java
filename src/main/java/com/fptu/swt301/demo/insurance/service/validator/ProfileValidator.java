package com.fptu.swt301.demo.insurance.service.validator;

import com.fptu.swt301.demo.insurance.config.ProfileConstants;
import com.fptu.swt301.demo.insurance.domain.model.BrokerProfile;
import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator cho Broker Profile
 * Tách validation logic ra khỏi domain model
 */
public class ProfileValidator {

    private static final Set<String> VALID_TITLES_SET = Arrays.stream(ProfileConstants.VALID_TITLES)
            .collect(Collectors.toSet());

    private static final Set<String> VALID_OCCUPATIONS_SET = Arrays.stream(ProfileConstants.VALID_OCCUPATIONS)
            .collect(Collectors.toSet());

    /**
     * Validate profile và throw ValidationException nếu có lỗi
     * 
     * @param profile Profile cần validate
     * @throws ValidationException nếu có lỗi validation
     */
    public void validate(BrokerProfile profile) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateUserId(profile.getUserId(), errors);
        validateTitle(profile.getTitle(), errors);
        validateName(profile.getFirstName(), "First name", errors);
        validateName(profile.getSurname(), "Surname", errors);
        validateDateOfBirth(profile.getDateOfBirth(), errors);
        validateLicenseInfo(profile.getLicenseType(), profile.getLicensePeriod(), errors);
        validateOccupation(profile.getOccupation(), errors);
        validateAddressFields(profile, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateUserId(String userId, List<String> errors) {
        if (userId == null || userId.trim().isEmpty()) {
            errors.add("User ID is required and cannot be empty");
        }
    }

    private void validateTitle(String title, List<String> errors) {
        if (title == null || title.trim().isEmpty()) {
            errors.add("Title is required");
        } else if (!isValidTitle(title)) {
            errors.add("Title must be one of: " + Arrays.toString(ProfileConstants.VALID_TITLES));
        }
    }

    private void validateName(String name, String fieldName, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add(fieldName + " is required and cannot be empty");
        }
    }

    private void validateDateOfBirth(LocalDate dateOfBirth, List<String> errors) {
        if (dateOfBirth == null) {
            errors.add("Date of birth is required");
            return;
        }

        LocalDate now = LocalDate.now();
        if (dateOfBirth.isAfter(now)) {
            errors.add("Date of birth cannot be in the future");
            return;
        }

        int age = Period.between(dateOfBirth, now).getYears();
        if (age < ProfileConstants.MINIMUM_AGE) {
            errors.add("Age must be at least " + ProfileConstants.MINIMUM_AGE + " years old");
        }
    }

    private void validateLicenseInfo(String licenseType, int licensePeriod, List<String> errors) {
        if (licenseType == null || licenseType.trim().isEmpty()) {
            errors.add("License type is required");
        } else {
            String typeTrimmed = licenseType.trim();
            if (!ProfileConstants.LICENSE_TYPE_FULL.equalsIgnoreCase(typeTrimmed)
                    && !ProfileConstants.LICENSE_TYPE_PROVISIONAL.equalsIgnoreCase(typeTrimmed)) {
                errors.add("License type must be 'Full' or 'Provisional'");
            }
        }

        if (licensePeriod < ProfileConstants.MIN_LICENSE_PERIOD) {
            errors.add("License period cannot be negative. Provided value: " + licensePeriod);
        }
        if (licensePeriod > ProfileConstants.MAX_LICENSE_PERIOD) {
            errors.add("License period cannot exceed " + ProfileConstants.MAX_LICENSE_PERIOD
                    + " years. Provided value: " + licensePeriod);
        }
    }

    private void validateOccupation(String occupation, List<String> errors) {
        if (occupation == null || occupation.trim().isEmpty()) {
            errors.add("Occupation is required");
        } else if (!isValidOccupation(occupation)) {
            errors.add("Occupation must be one of: " + Arrays.toString(ProfileConstants.VALID_OCCUPATIONS));
        }
    }

    private void validateAddressFields(BrokerProfile profile, List<String> errors) {
        if (profile.getStreet() != null && profile.getStreet().trim().isEmpty()) {
            errors.add("Street address cannot be only whitespace");
        }
        if (profile.getCity() != null && profile.getCity().trim().isEmpty()) {
            errors.add("City cannot be only whitespace");
        }
        if (profile.getCounty() != null && profile.getCounty().trim().isEmpty()) {
            errors.add("County cannot be only whitespace");
        }
        if (profile.getPostCode() != null && profile.getPostCode().trim().isEmpty()) {
            errors.add("Post code cannot be only whitespace");
        }
    }

    public static boolean isValidTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        return VALID_TITLES_SET.stream()
                .anyMatch(validTitle -> validTitle.equalsIgnoreCase(title.trim()));
    }

    public static boolean isValidOccupation(String occupation) {
        if (occupation == null || occupation.trim().isEmpty()) {
            return false;
        }
        return VALID_OCCUPATIONS_SET.stream()
                .anyMatch(validOccupation -> validOccupation.equalsIgnoreCase(occupation.trim()));
    }
}
