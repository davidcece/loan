package com.cece.lms.repository;

import com.cece.lms.entity.Loan;
import com.cece.lms.entity.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT l.* FROM loans l JOIN customers c ON l.customer_id = c.id " +
            " WHERE c.customer_number = :customerNumber " +
            " AND l.status = :status LIMIT 1", nativeQuery = true )
    Loan findByCustomerNumberAndStatus(@Param("customerNumber") String customerNumber, @Param("status") String status);

    @Query(value = "SELECT l.* FROM loans l JOIN customers c ON l.customer_id = c.id " +
            " WHERE c.customer_number = :customerNumber " +
            " ORDER BY l.created_at DESC LIMIT 1", nativeQuery = true)
    Loan findLastLoanStatus(@Param("customerNumber") String customerNumber);
}