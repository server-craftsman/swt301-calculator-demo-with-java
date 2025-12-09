package com.fptu.swt301.demo.insurance.calculator;

import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.time.LocalDate;
import java.util.Random;

/**
 * Class đại diện cho một Quote (báo giá) bảo hiểm đã được lưu
 * Bao gồm identification number để retrieve sau này
 */
public class InsuranceQuote {
    private String identificationNumber;
    private InsuranceQuoteRequest request;
    private double calculatedPremium;
    private LocalDate createdDate;
    private String userId;

    public InsuranceQuote() {
        this.createdDate = LocalDate.now();
        this.identificationNumber = generateIdentificationNumber();
    }

    public InsuranceQuote(InsuranceQuoteRequest request, double calculatedPremium) {
        this();
        this.request = request;
        // Use setter to trigger validation
        this.setCalculatedPremium(calculatedPremium);
    }

    /**
     * Tạo identification number ngẫu nhiên (5-6 chữ số)
     * Giống như ví dụ: 51700, 61699
     */
    private String generateIdentificationNumber() {
        Random random = new Random();
        int number = 10000 + random.nextInt(90000); // Tạo số từ 10000-99999
        return String.valueOf(number);
    }

    // Getters and Setters
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public InsuranceQuoteRequest getRequest() {
        return request;
    }

    public void setRequest(InsuranceQuoteRequest request) {
        this.request = request;
    }

    public double getCalculatedPremium() {
        return calculatedPremium;
    }

    public void setCalculatedPremium(double calculatedPremium) {
        if (calculatedPremium < 0) {
            throw new ValidationException(
                    "Calculated premium cannot be negative. Provided value: £" + calculatedPremium);
        }
        this.calculatedPremium = calculatedPremium;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * In thông tin quote theo format của hệ thống
     */
    public void printQuoteDetails() {
        System.out.println("========================================");
        System.out.println("Request Quotation Save");
        System.out.println("========================================");
        System.out.println("You have saved this quotation!");
        System.out.println("Your identification number is : " + identificationNumber);
        System.out.println("Please note it for future reference");
        System.out.println("========================================\n");
    }

    /**
     * In thông tin retrieve quote theo format của hệ thống
     */
    public void printRetrieveDetails() {
        System.out.println("========================================");
        System.out.println("Retrieve Quotation");
        System.out.println("========================================");
        System.out.println("Insurance Name\tValue");
        System.out.println("----------------------------------------");
        System.out.println("Breakdowncover\t" + request.getBreakdownCover());
        System.out.println("Windscreenrepair\t" + request.getWindscreenRepair());
        if (userId != null) {
            System.out.println("user_id\t" + userId);
        }
        System.out.println("incidents\t" + request.getNumberOfAccidents());
        System.out.println("Registration\t" + request.getRegistrationNumber());
        System.out.println("Annual mileage\t" + request.getTotalMileage());
        System.out.println("Estimated value\t" + (int) request.getEstimatedValue());
        System.out.println("Parking Location\t" + request.getParkingLocation());
        System.out.println("Start of policy\t" + request.getStartOfPolicy());
        System.out.println("Calculate Premium\t" + (int) calculatedPremium);
        System.out.println("========================================\n");
    }

    @Override
    public String toString() {
        return "InsuranceQuote{" +
                "identificationNumber='" + identificationNumber + '\'' +
                ", calculatedPremium=" + calculatedPremium +
                ", createdDate=" + createdDate +
                ", userId='" + userId + '\'' +
                '}';
    }
}
