package com.cece.lms.controller;

import com.cece.lms.entity.Loan;
import com.cece.lms.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {


    private final LoanService loanService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam String customerNumber) {
        // Subscription logic here
        return ResponseEntity.ok("Subscription successful for " + customerNumber);
    }

    @PostMapping("/request")
    public ResponseEntity<Loan> requestLoan(@RequestParam String customerNumber, @RequestParam Double amount) {
        Loan loan = loanService.requestLoan(customerNumber, amount);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/status/{customerNumber}")
    public ResponseEntity<Loan> getLoanStatus(@PathVariable String customerNumber) {
        Loan loan = loanService.getLoanStatus(customerNumber);
        return ResponseEntity.ok(loan);
    }
}
