package com.fptu.swt301.demo.insurance.domain.model;

import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.time.LocalDate;
import java.util.Random;
import java.util.Objects;

/**
 * Domain model cho Insurance Quote (báo giá bảo hiểm đã được lưu)
 * Bao gồm identification number để retrieve sau này
 */
public class InsuranceQuote {
    
    private final String identificationNumber;
    private final PremiumCalculationRequest request;
    private final double calculatedPremium;
    private final LocalDate createdDate;
    private final String userId;
    private final String registrationNumber;
    private final String startOfPolicy;

    private InsuranceQuote(Builder builder) {
        this.identificationNumber = builder.identificationNumber != null 
                ? builder.identificationNumber 
                : generateIdentificationNumber();
        this.request = builder.request;
        this.calculatedPremium = builder.calculatedPremium;
        this.createdDate = builder.createdDate != null 
                ? builder.createdDate 
                : LocalDate.now();
        this.userId = builder.userId;
        this.registrationNumber = builder.registrationNumber;
        this.startOfPolicy = builder.startOfPolicy;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Tạo identification number ngẫu nhiên (5 chữ số)
     * Giống như ví dụ: 51700, 61699
     */
    private String generateIdentificationNumber() {
        Random random = new Random();
        int number = 10000 + random.nextInt(90000); // Tạo số từ 10000-99999
        return String.valueOf(number);
    }

    // Getters
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public PremiumCalculationRequest getRequest() {
        return request;
    }

    public double getCalculatedPremium() {
        return calculatedPremium;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getStartOfPolicy() {
        return startOfPolicy;
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
        System.out.println("Breakdowncover\t" + request.getBreakdownCover().getName());
        System.out.println("Windscreenrepair\t" + (request.isWindscreenRepair() ? "Yes" : "No"));
        if (userId != null) {
            System.out.println("user_id\t" + userId);
        }
        System.out.println("incidents\t" + request.getNumberOfAccidents());
        System.out.println("Registration\t" + (registrationNumber != null ? registrationNumber : ""));
        System.out.println("Annual mileage\t" + request.getTotalMileage());
        System.out.println("Estimated value\t" + (int) request.getEstimatedValue());
        System.out.println("Parking Location\t" + request.getParkingLocation());
        System.out.println("Start of policy\t" + (startOfPolicy != null ? startOfPolicy : ""));
        System.out.println("Calculate Premium\t" + (int) calculatedPremium);
        System.out.println("========================================\n");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsuranceQuote that = (InsuranceQuote) o;
        return Objects.equals(identificationNumber, that.identificationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificationNumber);
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

    /**
     * Builder pattern cho InsuranceQuote
     */
    public static class Builder {
        private String identificationNumber;
        private PremiumCalculationRequest request;
        private double calculatedPremium;
        private LocalDate createdDate;
        private String userId;
        private String registrationNumber;
        private String startOfPolicy;

        public Builder identificationNumber(String identificationNumber) {
            this.identificationNumber = identificationNumber;
            return this;
        }

        public Builder request(PremiumCalculationRequest request) {
            this.request = request;
            return this;
        }

        public Builder calculatedPremium(double calculatedPremium) {
            if (calculatedPremium < 0) {
                throw new ValidationException(
                        "Calculated premium cannot be negative. Provided value: £" + calculatedPremium);
            }
            this.calculatedPremium = calculatedPremium;
            return this;
        }

        public Builder createdDate(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder startOfPolicy(String startOfPolicy) {
            this.startOfPolicy = startOfPolicy;
            return this;
        }

        public InsuranceQuote build() {
            if (request == null) {
                throw new IllegalArgumentException("Request cannot be null");
            }
            return new InsuranceQuote(this);
        }
    }
}

