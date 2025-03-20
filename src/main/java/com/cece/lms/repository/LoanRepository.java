package com.cece.lms.repository;

import com.cece.lms.entity.Loan;
import com.cece.lms.entity.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByCustomerNumberAndStatus(String customerNumber, LoanStatus status);
}
