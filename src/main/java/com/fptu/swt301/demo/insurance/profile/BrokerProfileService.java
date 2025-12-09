package com.fptu.swt301.demo.insurance.profile;

import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service để quản lý Broker Profiles
 * Simulate database operations
 */
public class BrokerProfileService {
    // Simulate database với HashMap
    private Map<String, BrokerProfile> profileDatabase;

    public BrokerProfileService() {
        this.profileDatabase = new HashMap<>();
    }

    /**
     * View profile của broker theo userId
     * 
     * @param userId User ID của broker
     * @return BrokerProfile nếu tồn tại, null nếu không
     */
    public BrokerProfile viewProfile(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        BrokerProfile profile = profileDatabase.get(userId);

        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for user ID: " + userId);
        }

        return profile;
    }

    /**
     * Create new profile
     * 
     * @param profile Profile cần tạo
     * @return true nếu tạo thành công, false nếu đã tồn tại
     */
    public boolean createProfile(BrokerProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        // Validate profile - will throw ValidationException with all errors
        try {
            profile.validate();
        } catch (ValidationException e) {
            throw e; // Re-throw to preserve all validation errors
        }

        String userId = profile.getUserId();
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (profileDatabase.containsKey(userId)) {
            return false; // Profile already exists
        }

        profileDatabase.put(userId, profile);
        return true;
    }

    /**
     * Update existing profile
     * 
     * @param profile Profile với thông tin mới
     * @return true nếu update thành công, false nếu không tìm thấy
     */
    public boolean updateProfile(BrokerProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        // Validate profile - will throw ValidationException with all errors
        try {
            profile.validate();
        } catch (ValidationException e) {
            throw e; // Re-throw to preserve all validation errors
        }

        String userId = profile.getUserId();
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (!profileDatabase.containsKey(userId)) {
            return false; // Profile doesn't exist
        }

        profileDatabase.put(userId, profile);
        return true;
    }

    /**
     * Delete profile
     * 
     * @param userId User ID
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public boolean deleteProfile(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return profileDatabase.remove(userId) != null;
    }

    /**
     * Check if profile exists
     * 
     * @param userId User ID
     * @return true nếu tồn tại, false nếu không
     */
    public boolean profileExists(String userId) {
        return userId != null && profileDatabase.containsKey(userId);
    }

    /**
     * Get total number of profiles
     * 
     * @return số lượng profiles
     */
    public int getProfileCount() {
        return profileDatabase.size();
    }

    /**
     * Clear all profiles (for testing)
     */
    public void clearAllProfiles() {
        profileDatabase.clear();
    }
}
