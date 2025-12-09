package com.fptu.swt301.demo.insurance.domain.model;

import com.fptu.swt301.demo.insurance.domain.valueobject.PhoneNumber;
import com.fptu.swt301.demo.insurance.domain.valueobject.LicenseInfo;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Domain model cho Broker Profile
 * Sử dụng Value Objects để đảm bảo tính toàn vẹn dữ liệu
 */
public class BrokerProfile {

    private final String userId;
    private final String title;
    private final String firstName;
    private final String surname;
    private final PhoneNumber phone;
    private final LocalDate dateOfBirth;
    private final LicenseInfo licenseInfo;
    private final String occupation;
    private final Address address;
    private final String driverHistory;

    private BrokerProfile(Builder builder) {
        this.userId = builder.userId;
        this.title = builder.title;
        this.firstName = builder.firstName;
        this.surname = builder.surname;
        this.phone = builder.phone;
        this.dateOfBirth = builder.dateOfBirth;
        this.licenseInfo = builder.licenseInfo;
        this.occupation = builder.occupation;
        this.address = builder.address;
        this.driverHistory = builder.driverHistory;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public String getPhoneValue() {
        return phone != null ? phone.getValue() : null;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LicenseInfo getLicenseInfo() {
        return licenseInfo;
    }

    public String getLicenseType() {
        return licenseInfo != null ? licenseInfo.getType() : null;
    }

    public int getLicensePeriod() {
        return licenseInfo != null ? licenseInfo.getPeriod() : 0;
    }

    public String getOccupation() {
        return occupation;
    }

    public Address getAddress() {
        return address;
    }

    public String getStreet() {
        return address != null ? address.getStreet() : null;
    }

    public String getCity() {
        return address != null ? address.getCity() : null;
    }

    public String getCounty() {
        return address != null ? address.getCounty() : null;
    }

    public String getPostCode() {
        return address != null ? address.getPostCode() : null;
    }

    public String getDriverHistory() {
        return driverHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BrokerProfile that = (BrokerProfile) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    /**
     * Builder pattern cho BrokerProfile
     */
    public static class Builder {
        private String userId;
        private String title;
        private String firstName;
        private String surname;
        private PhoneNumber phone;
        private LocalDate dateOfBirth;
        private LicenseInfo licenseInfo;
        private String occupation;
        private Address address;
        private String driverHistory;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder phone(PhoneNumber phone) {
            this.phone = phone;
            return this;
        }

        public Builder phone(String phone) {
            try {
                this.phone = PhoneNumber.of(phone);
            } catch (Exception e) {
                // Will be validated later
                this.phone = null;
            }
            return this;
        }

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder licenseInfo(LicenseInfo licenseInfo) {
            this.licenseInfo = licenseInfo;
            return this;
        }

        public Builder licenseType(String licenseType) {
            if (this.licenseInfo == null) {
                try {
                    this.licenseInfo = LicenseInfo.of(licenseType, 0);
                } catch (Exception e) {
                    // Will be validated later
                }
            }
            return this;
        }

        public Builder licensePeriod(int licensePeriod) {
            if (this.licenseInfo != null) {
                try {
                    this.licenseInfo = LicenseInfo.of(this.licenseInfo.getType(), licensePeriod);
                } catch (Exception e) {
                    // Will be validated later
                }
            }
            return this;
        }

        public Builder occupation(String occupation) {
            this.occupation = occupation;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder street(String street) {
            if (this.address == null) {
                this.address = new Address();
            }
            this.address.setStreet(street);
            return this;
        }

        public Builder city(String city) {
            if (this.address == null) {
                this.address = new Address();
            }
            this.address.setCity(city);
            return this;
        }

        public Builder county(String county) {
            if (this.address == null) {
                this.address = new Address();
            }
            this.address.setCounty(county);
            return this;
        }

        public Builder postCode(String postCode) {
            if (this.address == null) {
                this.address = new Address();
            }
            this.address.setPostCode(postCode);
            return this;
        }

        public Builder driverHistory(String driverHistory) {
            this.driverHistory = driverHistory;
            return this;
        }

        public BrokerProfile build() {
            return new BrokerProfile(this);
        }
    }

    /**
     * Value Object cho Address
     */
    public static class Address {
        private String street;
        private String city;
        private String county;
        private String postCode;

        public Address() {
        }

        public Address(String street, String city, String county, String postCode) {
            this.street = street;
            this.city = city;
            this.county = county;
            this.postCode = postCode;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }
    }
}
