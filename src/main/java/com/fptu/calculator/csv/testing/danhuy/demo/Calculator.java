package com.fptu.calculator.csv.testing.danhuy.demo;

public class Calculator {

    /**
     * Phép cộng hai số
     * 
     * @param a số thứ nhất
     * @param b số thứ hai
     * @return tổng của a và b
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Phép trừ hai số
     * 
     * @param a số bị trừ
     * @param b số trừ
     * @return hiệu của a và b
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Phép nhân hai số
     * 
     * @param a số thứ nhất
     * @param b số thứ hai
     * @return tích của a và b
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Phép chia hai số
     * 
     * @param a số bị chia
     * @param b số chia
     * @return thương của a và b
     * @throws ArithmeticException nếu b = 0
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }
}
