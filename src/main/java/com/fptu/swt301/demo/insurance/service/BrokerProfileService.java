package com.fptu.swt301.demo.insurance.service;

import com.fptu.swt301.demo.insurance.domain.model.BrokerProfile;
import com.fptu.swt301.demo.insurance.exception.ValidationException;
import com.fptu.swt301.demo.insurance.repository.BrokerProfileRepository;
import com.fptu.swt301.demo.insurance.service.validator.ProfileValidator;

/**
 * Service để quản lý Broker Profiles
 * Sử dụng Repository pattern và Validator pattern
 */
public class BrokerProfileService {

    private final BrokerProfileRepository repository;
    private final ProfileValidator validator;

    public BrokerProfileService() {
        this.repository = new com.fptu.swt301.demo.insurance.repository.InMemoryBrokerProfileRepository();
        this.validator = new ProfileValidator();
    }

    public BrokerProfileService(BrokerProfileRepository repository, ProfileValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    /**
     * View profile của broker theo userId
     * 
     * @param userId User ID của broker
     * @return BrokerProfile nếu tồn tại
     * @throws IllegalArgumentException nếu userId null/empty hoặc không tìm thấy
     */
    public BrokerProfile viewProfile(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return repository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for user ID: " + userId));
    }

    /**
     * Create new profile
     * 
     * @param profile Profile cần tạo
     * @return true nếu tạo thành công, false nếu đã tồn tại
     * @throws ValidationException nếu profile không hợp lệ
     */
    public boolean createProfile(BrokerProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        // Validate profile
        validator.validate(profile);

        // Check if already exists
        if (repository.exists(profile.getUserId())) {
            return false;
        }

        // Save to repository
        return repository.save(profile);
    }

    /**
     * Update existing profile
     * 
     * @param profile Profile với thông tin mới
     * @return true nếu update thành công, false nếu không tìm thấy
     * @throws ValidationException nếu profile không hợp lệ
     */
    public boolean updateProfile(BrokerProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        // Validate profile
        validator.validate(profile);

        // Check if exists
        if (!repository.exists(profile.getUserId())) {
            return false;
        }

        // Update in repository
        return repository.update(profile);
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

        return repository.delete(userId);
    }

    /**
     * Check if profile exists
     * 
     * @param userId User ID
     * @return true nếu tồn tại, false nếu không
     */
    public boolean profileExists(String userId) {
        return repository.exists(userId);
    }

    /**
     * Get total number of profiles
     * 
     * @return số lượng profiles
     */
    public int getProfileCount() {
        return (int) repository.count();
    }

    /**
     * Clear all profiles (for testing)
     */
    public void clearAllProfiles() {
        repository.deleteAll();
    }
}
