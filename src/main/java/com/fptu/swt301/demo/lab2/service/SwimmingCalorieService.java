package com.fptu.swt301.demo.lab2.service;

import com.fptu.swt301.demo.lab2.domain.model.CalorieCalculationRequest;
import com.fptu.swt301.demo.lab2.repository.CalorieCalculationRepository;
import com.fptu.swt301.demo.lab2.repository.InMemoryCalorieCalculationRepository;

/**
 * Service để tính toán lượng calo tiêu thụ khi bơi
 * 
 * Service layer chịu trách nhiệm:
 * - Business logic và orchestration
 * - Validation (thông qua domain model)
 * - Delegate calculation logic cho Repository
 * 
 * Repository pattern giúp:
 * - Tách biệt data access và calculation logic
 * - Dễ dàng thay đổi implementation (in-memory, database, cache, etc.)
 * - Dễ dàng test với mock repository
 */
public class SwimmingCalorieService {

    private final CalorieCalculationRepository repository;

    /**
     * Constructor với default repository (InMemory)
     */
    public SwimmingCalorieService() {
        this.repository = new InMemoryCalorieCalculationRepository();
    }

    /**
     * Constructor với custom repository (dependency injection)
     * Cho phép inject repository để dễ dàng test và mở rộng
     */
    public SwimmingCalorieService(CalorieCalculationRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
    }

    /**
     * Tính lượng calo tiêu thụ dựa trên request
     * Delegate cho repository để thực hiện calculation
     * 
     * @param request Calorie calculation request
     * @return Total calories burned (rounded to 2 decimal places)
     */
    public double calculateCaloriesBurned(CalorieCalculationRequest request) {
        // Validation được thực hiện trong request.build()
        // Service chỉ cần delegate cho repository
        return repository.calculateCaloriesBurned(request);
    }

    /**
     * Tính calories per minute (không nhân với duration)
     * Delegate cho repository để thực hiện calculation
     * 
     * @param request Calorie calculation request
     * @return Calories per minute (rounded to 2 decimal places)
     */
    public double calculateCaloriesPerMinute(CalorieCalculationRequest request) {
        return repository.calculateCaloriesPerMinute(request);
    }

    /**
     * Convenience method để tính calories với các tham số riêng lẻ
     * Tạo request và delegate cho repository
     */
    public double calculateCaloriesBurned(
            String swimmingStyleName,
            double durationMin,
            double bodyWeightKg) {

        CalorieCalculationRequest request = CalorieCalculationRequest.builder()
                .swimmingStyle(swimmingStyleName)
                .durationMin(durationMin)
                .bodyWeightKg(bodyWeightKg)
                .build();

        return calculateCaloriesBurned(request);
    }

    /**
     * Lấy lịch sử tính toán từ repository
     * 
     * @return List các calculation history
     */
    public java.util.List<CalorieCalculationRepository.CalculationHistory> getCalculationHistory() {
        return repository.getAllCalculationHistory();
    }

    /**
     * Xóa lịch sử tính toán
     */
    public void clearHistory() {
        repository.clearHistory();
    }

    /**
     * Đếm số lượng calculations đã thực hiện
     * 
     * @return Số lượng calculations
     */
    public long getCalculationCount() {
        return repository.count();
    }

    /**
     * Tính calories burned với giá trị chính xác (không làm tròn)
     * Dùng cho test và debug
     * 
     * @param request Calorie calculation request
     * @return Total calories burned (không làm tròn)
     */
    public double calculateCaloriesBurnedExact(CalorieCalculationRequest request) {
        return repository.calculateCaloriesBurnedExact(request);
    }

    /**
     * Tính calories per minute với giá trị chính xác (không làm tròn)
     * Dùng cho test và debug
     * 
     * @param request Calorie calculation request
     * @return Calories per minute (không làm tròn)
     */
    public double calculateCaloriesPerMinuteExact(CalorieCalculationRequest request) {
        return repository.calculateCaloriesPerMinuteExact(request);
    }

    /**
     * Tính calories và trả về formatted string (giống web production)
     * 
     * @param request           Calorie calculation request
     * @param useEuropeanFormat Sử dụng format châu Âu (dấu phẩy)
     * @return Formatted string với unit (ví dụ: "507.15 kcal" hoặc "507,15 kcal")
     */
    public String calculateCaloriesBurnedFormatted(CalorieCalculationRequest request, boolean useEuropeanFormat) {
        double calories = calculateCaloriesBurned(request);
        return CalorieFormatter.formatWithUnit(calories, useEuropeanFormat);
    }

    /**
     * Tính calories per minute và trả về formatted string (giống web production)
     * 
     * @param request           Calorie calculation request
     * @param useEuropeanFormat Sử dụng format châu Âu (dấu phẩy)
     * @return Formatted string với unit (ví dụ: "16.91 kcal/min" hoặc "16,91
     *         kcal/min")
     */
    public String calculateCaloriesPerMinuteFormatted(CalorieCalculationRequest request, boolean useEuropeanFormat) {
        double caloriesPerMin = calculateCaloriesPerMinute(request);
        String formatted = useEuropeanFormat
                ? CalorieFormatter.formatCaloriesPerMinuteEuropean(caloriesPerMin)
                : CalorieFormatter.formatCaloriesPerMinute(caloriesPerMin);
        return formatted + " kcal/min";
    }

    /**
     * Tính calories với detailed format (nhiều chữ số thập phân hơn, giống web
     * production)
     * Web production hiển thị với nhiều chữ số thập phân hơn cho giá trị nhỏ
     * 
     * @param request           Calorie calculation request
     * @param useEuropeanFormat Sử dụng format châu Âu (dấu phẩy)
     * @return Formatted string với unit (ví dụ: "0.16905 kcal" hoặc "0,16905 kcal")
     */
    public String calculateCaloriesBurnedDetailedFormatted(CalorieCalculationRequest request,
            boolean useEuropeanFormat) {
        double calories = calculateCaloriesBurned(request);
        String formatted = useEuropeanFormat
                ? CalorieFormatter.formatTotalCaloriesEuropean(calories)
                : CalorieFormatter.formatTotalCaloriesDetailed(calories);
        return formatted + " kcal";
    }
}
