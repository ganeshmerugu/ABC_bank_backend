package com.ABC.Bank.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@CrossOrigin()

@Data

@Entity

public class LoanApplication {

    @TableGenerator(name = "LoanApplication", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "Addr_Gen", initialValue = 202300, allocationSize = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LoanApplication")
    private long  applicationId;
    @NotBlank
    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String middleName;

    @NotBlank
    @Size(max = 255)
    private String lastName;

    @Past @DateTimeFormat
    private LocalDate dateOfBirth;
    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    public LocalDate submissionDate;
    @PrePersist
    private void onCreate(){
        submissionDate= LocalDate.now();
    }
    private String status;
    @NotBlank
    private String maritalStatus;

    @NotBlank
    private String ssnNumber;

    @NotNull
    @Positive
    private BigDecimal loanAmount;

    @NotBlank
    private String loanPurpose;

    private String description;

    @NotBlank(message = "Address Line 1 is required")
    @Size(max = 255, message = "Address Line 1 must be less than 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address Line 2 must be less than 255 characters")
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 255, message = "City must be less than 255 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 255, message = "State must be less than 255 characters")
    private String state;

    @NotBlank(message = "Postal Code is required")
    @Pattern(regexp = "\\d{5}", message = "Postal Code must be numeric and exactly 5 digits")
    private String postalCode;

    @NotBlank(message = "Home Phone is required")
    @Pattern(regexp = "\\d{10}", message = "Home Phone must be numeric and exactly 10 digits")
    private String homePhone;

    @Pattern(regexp = "\\d{10}", message = "Office Phone must be numeric and exactly 10 digits")
    private String officePhone;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile Number must be numeric and exactly 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Email Address is required")
    @Email(message = "Email Address must be a valid email address")
    private String emailAddress;

    @NotBlank(message = "Employer name is required")
    @Size(max = 255, message = "Employer name must be less than 255 characters")
    private String employerName;

    @NotNull(message = "Annual Salary is required")
    @Positive(message = "Annual Salary must be numeric and greater than zero")
    private BigDecimal annualSalary;

    private String designation;

    @NotBlank(message = "Employer Address Line 1 is required")
    @Size(max = 255, message = "Employer Address Line 1 must be less than 255 characters")
    private String employerAddressLine1;

    @Size(max = 255, message = "Employer Address Line 2 must be less than 255 characters")
    private String employerAddressLine2;

    @NotBlank(message = "Employer City is required")
    @Size(max = 255, message = "Employer City must be less than 255 characters")
    private String employerCity;

    @NotBlank(message = "Employer State is required")
    @Size(max = 255, message = "Employer State must be less than 255 characters")
    private String employerState;

    @NotBlank(message = "Employer Postal Code is required")
    @Pattern(regexp = "\\d{5}", message = "Employer Postal Code must be numeric and exactly 5 digits")
    private String employerPostalCode;

    private String workExperienceYear;
    private String workExperienceMonth;
    private String declineReasons;
    public int score;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
