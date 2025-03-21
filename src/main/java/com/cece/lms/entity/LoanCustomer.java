package com.cece.lms.entity;

import javax.persistence.*;

import com.credable.soap.kyc.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String customerNumber;
    private Date dob;
    private String email;
    private String firstName;
    private String gender;
    private Long cbsId;
    private String idNumber;
    private String idType;
    private String lastName;
    private String middleName;
    private String mobile;
    private double monthlyIncome;
    private String status;

    public LoanCustomer(Customer cbsCustomer) {
        this.customerNumber = cbsCustomer.getCustomerNumber();
        this.dob = cbsCustomer.getDob().toGregorianCalendar().getTime();
        this.email = cbsCustomer.getEmail();
        this.firstName = cbsCustomer.getFirstName();
        this.gender = cbsCustomer.getGender().value();
        this.cbsId = cbsCustomer.getId();
        this.idNumber = cbsCustomer.getIdNumber();
        this.idType = cbsCustomer.getIdType().value();
        this.lastName = cbsCustomer.getLastName();
        this.middleName = cbsCustomer.getMiddleName();
        this.mobile = cbsCustomer.getMobile();
        this.monthlyIncome = cbsCustomer.getMonthlyIncome();
        this.status = cbsCustomer.getStatus().value();
    }
}
