package com.cece.lms.repository;

import com.cece.lms.entity.LoanCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanCustomerRepository extends JpaRepository<LoanCustomer, Integer> {
    LoanCustomer findByCustomerNumber(String customerNumber);
}
