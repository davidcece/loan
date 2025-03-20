package com.cece.lms.service;

import com.cece.lms.entity.Loan;
import com.cece.lms.entity.enums.LoanStatus;
import com.cece.lms.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public Loan requestLoan(String customerNumber, Double amount) {
        Optional<Loan> existingLoan = loanRepository.findByCustomerNumberAndStatus(customerNumber, LoanStatus.INITIATED);
        if (existingLoan.isPresent()) {
            throw new RuntimeException("Active loan already exists.");
        }

        Loan loan = new Loan();
        loan.setCustomerNumber(customerNumber);
        loan.setAmount(amount);
        loan.setStatus(LoanStatus.INITIATED);
        loan.setCreatedAt(LocalDateTime.now());

        // TODO: Integrate with Scoring Engine and CBS APIs
        return loanRepository.save(loan);
    }

    public Loan getLoanStatus(String customerNumber) {
        return loanRepository.findByCustomerNumberAndStatus(customerNumber, LoanStatus.INITIATED)
                .orElseThrow(() -> new RuntimeException("No active loan found."));
    }


}

