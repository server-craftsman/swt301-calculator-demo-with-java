package com.fptu.swt301.demo.insurance.repository;

import com.fptu.swt301.demo.insurance.domain.model.BrokerProfile;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation của BrokerProfileRepository
 * Sử dụng HashMap để simulate database
 */
public class InMemoryBrokerProfileRepository implements BrokerProfileRepository {

    private final Map<String, BrokerProfile> profiles;

    public InMemoryBrokerProfileRepository() {
        this.profiles = new HashMap<>();
    }

    @Override
    public Optional<BrokerProfile> findByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(profiles.get(userId));
    }

    @Override
    public boolean save(BrokerProfile profile) {
        if (profile == null || profile.getUserId() == null) {
            return false;
        }
        String userId = profile.getUserId();
        if (profiles.containsKey(userId)) {
            return false; // Already exists
        }
        profiles.put(userId, profile);
        return true;
    }

    @Override
    public boolean update(BrokerProfile profile) {
        if (profile == null || profile.getUserId() == null) {
            return false;
        }
        String userId = profile.getUserId();
        if (!profiles.containsKey(userId)) {
            return false; // Not found
        }
        profiles.put(userId, profile);
        return true;
    }

    @Override
    public boolean delete(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        return profiles.remove(userId) != null;
    }

    @Override
    public boolean exists(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        return profiles.containsKey(userId);
    }

    @Override
    public long count() {
        return profiles.size();
    }

    @Override
    public void deleteAll() {
        profiles.clear();
    }
}
