package com.fptu.swt301.demo.lab2.repository;

import com.fptu.swt301.demo.lab2.domain.model.CalorieCalculationRequest;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho Calorie Calculation
 * Tách data access và calculation logic ra khỏi business logic
 * 
 * Repository pattern giúp:
 * - Tách biệt data access logic
 * - Dễ dàng thay đổi implementation (in-memory, database, cache, etc.)
 * - Dễ dàng test với mock repository
 */
public interface CalorieCalculationRepository {

    /**
     * Tính toán lượng calo tiêu thụ dựa trên request
     * 
     * @param request Calorie calculation request
     * @return Total calories burned (rounded to 2 decimal places)
     */
    double calculateCaloriesBurned(CalorieCalculationRequest request);

    /**
     * Tính calories per minute (không nhân với duration)
     * 
     * @param request Calorie calculation request
     * @return Calories per minute (rounded to 2 decimal places)
     */
    double calculateCaloriesPerMinute(CalorieCalculationRequest request);

    /**
     * Tính calories burned với giá trị chính xác (không làm tròn)
     * Dùng cho test và debug để thấy giá trị chính xác
     * 
     * @param request Calorie calculation request
     * @return Total calories burned (không làm tròn)
     */
    double calculateCaloriesBurnedExact(CalorieCalculationRequest request);

    /**
     * Tính calories per minute với giá trị chính xác (không làm tròn)
     * Dùng cho test và debug để thấy giá trị chính xác
     * 
     * @param request Calorie calculation request
     * @return Calories per minute (không làm tròn)
     */
    double calculateCaloriesPerMinuteExact(CalorieCalculationRequest request);

    /**
     * Lưu lịch sử tính toán (optional - có thể implement để log hoặc cache)
     * 
     * @param request Request đã tính toán
     * @param result  Kết quả tính toán
     * @return true nếu lưu thành công
     */
    boolean saveCalculationHistory(CalorieCalculationRequest request, double result);

    /**
     * Lấy lịch sử tính toán (optional - có thể implement để cache hoặc retrieve)
     * 
     * @param request Request cần tìm
     * @return Optional<Double> - kết quả nếu đã tính toán trước đó
     */
    Optional<Double> findCalculationHistory(CalorieCalculationRequest request);

    /**
     * Lấy tất cả lịch sử tính toán
     * 
     * @return List các kết quả đã lưu
     */
    List<CalculationHistory> getAllCalculationHistory();

    /**
     * Xóa tất cả lịch sử (chủ yếu dùng cho testing)
     */
    void clearHistory();

    /**
     * Đếm số lượng calculations đã lưu
     * 
     * @return Số lượng calculations
     */
    long count();

    /**
     * Inner class để lưu trữ lịch sử tính toán
     */
    class CalculationHistory {
        private final CalorieCalculationRequest request;
        private final double result;
        private final long timestamp;

        public CalculationHistory(CalorieCalculationRequest request, double result) {
            this.request = request;
            this.result = result;
            this.timestamp = System.currentTimeMillis();
        }

        public CalorieCalculationRequest getRequest() {
            return request;
        }

        public double getResult() {
            return result;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return String.format(
                    "CalculationHistory{style=%s, duration=%.2f min, weight=%.2f kg, result=%.2f kcal, timestamp=%d}",
                    request.getSwimmingStyle().getDisplayName(),
                    request.getDurationMin(),
                    request.getBodyWeightKg(),
                    result,
                    timestamp);
        }
    }
}
