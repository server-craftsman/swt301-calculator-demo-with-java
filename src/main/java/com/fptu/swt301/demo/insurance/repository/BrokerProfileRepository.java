package com.fptu.swt301.demo.insurance.repository;

import com.fptu.swt301.demo.insurance.domain.model.BrokerProfile;
import java.util.Optional;

/**
 * Repository interface cho Broker Profile
 * Tách data access logic ra khỏi business logic
 */
public interface BrokerProfileRepository {

    /**
     * Tìm profile theo userId
     * 
     * @param userId User ID
     * @return Optional<BrokerProfile> - empty nếu không tìm thấy
     */
    Optional<BrokerProfile> findByUserId(String userId);

    /**
     * Lưu profile mới
     * 
     * @param profile Profile cần lưu
     * @return true nếu lưu thành công, false nếu userId đã tồn tại
     */
    boolean save(BrokerProfile profile);

    /**
     * Cập nhật profile đã tồn tại
     * 
     * @param profile Profile với thông tin mới
     * @return true nếu update thành công, false nếu không tìm thấy
     */
    boolean update(BrokerProfile profile);

    /**
     * Xóa profile
     * 
     * @param userId User ID
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    boolean delete(String userId);

    /**
     * Kiểm tra profile có tồn tại không
     * 
     * @param userId User ID
     * @return true nếu tồn tại, false nếu không
     */
    boolean exists(String userId);

    /**
     * Đếm tổng số profiles
     * 
     * @return Số lượng profiles
     */
    long count();

    /**
     * Xóa tất cả profiles (chủ yếu dùng cho testing)
     */
    void deleteAll();
}
