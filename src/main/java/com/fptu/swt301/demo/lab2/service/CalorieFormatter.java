package com.fptu.swt301.demo.lab2.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class để format calories theo chuẩn web production
 * 
 * Web production hiển thị:
 * - Calories per minute: 2 chữ số thập phân (ví dụ: 16.91)
 * - Total calories: có thể hiển thị nhiều chữ số thập phân hơn (ví dụ: 0.16905)
 * - Sử dụng dấu phẩy (,) cho số thập phân trong một số locale
 */
public class CalorieFormatter {

    private static final DecimalFormat DECIMAL_FORMAT_2 = new DecimalFormat("0.00");
    private static final DecimalFormat DECIMAL_FORMAT_5 = new DecimalFormat("0.#####");

    // Format với dấu phẩy (European format) - giống web production
    private static final DecimalFormat DECIMAL_FORMAT_EUROPEAN_2;
    private static final DecimalFormat DECIMAL_FORMAT_EUROPEAN_5;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DECIMAL_FORMAT_EUROPEAN_2 = new DecimalFormat("0.00", symbols);
        DECIMAL_FORMAT_EUROPEAN_5 = new DecimalFormat("0.#####", symbols);
    }

    /**
     * Format calories per minute với 2 chữ số thập phân (giống web production)
     * 
     * @param caloriesPerMinute Calories per minute
     * @return Formatted string (ví dụ: "16.91" hoặc "16,91")
     */
    public static String formatCaloriesPerMinute(double caloriesPerMinute) {
        return DECIMAL_FORMAT_2.format(caloriesPerMinute);
    }

    /**
     * Format total calories với 2 chữ số thập phân (cho lưu trữ và so sánh)
     * 
     * @param totalCalories Total calories burned
     * @return Formatted string (ví dụ: "507.15")
     */
    public static String formatTotalCalories(double totalCalories) {
        return DECIMAL_FORMAT_2.format(totalCalories);
    }

    /**
     * Format total calories với 5 chữ số thập phân (cho hiển thị chi tiết, giống
     * web production)
     * Web production hiển thị với nhiều chữ số thập phân hơn cho giá trị nhỏ
     * 
     * @param totalCalories Total calories burned
     * @return Formatted string (ví dụ: "0.16905")
     */
    public static String formatTotalCaloriesDetailed(double totalCalories) {
        return DECIMAL_FORMAT_5.format(totalCalories);
    }

    /**
     * Format với dấu phẩy (European format) - giống web production UI
     * 
     * @param caloriesPerMinute Calories per minute
     * @return Formatted string với dấu phẩy (ví dụ: "16,91")
     */
    public static String formatCaloriesPerMinuteEuropean(double caloriesPerMinute) {
        return DECIMAL_FORMAT_EUROPEAN_2.format(caloriesPerMinute);
    }

    /**
     * Format total calories với dấu phẩy (European format) - giống web production
     * UI
     * 
     * @param totalCalories Total calories burned
     * @return Formatted string với dấu phẩy (ví dụ: "0,16905")
     */
    public static String formatTotalCaloriesEuropean(double totalCalories) {
        return DECIMAL_FORMAT_EUROPEAN_5.format(totalCalories);
    }

    /**
     * Format calories với unit
     * 
     * @param calories          Calories value
     * @param useEuropeanFormat Sử dụng format châu Âu (dấu phẩy)
     * @return Formatted string với unit (ví dụ: "507.15 kcal" hoặc "507,15 kcal")
     */
    public static String formatWithUnit(double calories, boolean useEuropeanFormat) {
        if (useEuropeanFormat) {
            return formatTotalCaloriesEuropean(calories) + " kcal";
        } else {
            return formatTotalCalories(calories) + " kcal";
        }
    }
}
