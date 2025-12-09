package com.fptu.swt301.demo.insurance.calculator;

import com.fptu.swt301.demo.insurance.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class đại diện cho một yêu cầu báo giá bảo hiểm
 */
public class InsuranceQuoteRequest {
    private String breakdownCover;
    private String windscreenRepair;
    private int numberOfAccidents;
    private int totalMileage;
    private double estimatedValue;
    private String parkingLocation;
    private String registrationNumber;
    private String startOfPolicy;

    public InsuranceQuoteRequest() {
    }

    public InsuranceQuoteRequest(String breakdownCover, String windscreenRepair,
            int numberOfAccidents, int totalMileage,
            double estimatedValue, String parkingLocation) {
        this.breakdownCover = breakdownCover;
        this.windscreenRepair = windscreenRepair;
        this.numberOfAccidents = numberOfAccidents;
        this.totalMileage = totalMileage;
        this.estimatedValue = estimatedValue;
        this.parkingLocation = parkingLocation;
        // Validate all fields - will throw ValidationException with all errors
        this.validate();
    }

    // Getters and Setters
    public String getBreakdownCover() {
        return breakdownCover;
    }

    public void setBreakdownCover(String breakdownCover) {
        this.breakdownCover = breakdownCover;
    }

    public String getWindscreenRepair() {
        return windscreenRepair;
    }

    public void setWindscreenRepair(String windscreenRepair) {
        this.windscreenRepair = windscreenRepair;
    }

    public int getNumberOfAccidents() {
        return numberOfAccidents;
    }

    public void setNumberOfAccidents(int numberOfAccidents) {
        this.numberOfAccidents = numberOfAccidents;
    }

    public int getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(int totalMileage) {
        this.totalMileage = totalMileage;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getStartOfPolicy() {
        return startOfPolicy;
    }

    public void setStartOfPolicy(String startOfPolicy) {
        this.startOfPolicy = startOfPolicy;
    }

    /**
     * Validate all fields in the request
     * Collects all validation errors and throws ValidationException with all errors
     * 
     * @throws ValidationException containing all validation errors
     */
    public void validate() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (numberOfAccidents < 0) {
            errors.add("Number of accidents cannot be negative. Provided value: " + numberOfAccidents);
        }
        if (totalMileage < 0) {
            errors.add("Total mileage cannot be negative. Provided value: " + totalMileage);
        }
        if (estimatedValue < 0) {
            errors.add("Estimated value cannot be negative. Provided value: £" + estimatedValue);
        }
        if (estimatedValue < 100) {
            errors.add("Estimated value must be at least £100. Provided value: £" + estimatedValue);
        }

        // Throw ValidationException with all errors if any
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public String toString() {
        return "InsuranceQuoteRequest{" +
                "breakdownCover='" + breakdownCover + '\'' +
                ", windscreenRepair='" + windscreenRepair + '\'' +
                ", numberOfAccidents=" + numberOfAccidents +
                ", totalMileage=" + totalMileage +
                ", estimatedValue=" + estimatedValue +
                ", parkingLocation='" + parkingLocation + '\'' +
                '}';
    }
}
