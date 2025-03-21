package com.cece.lms.controller;

import com.cece.lms.entity.Loan;
import com.cece.lms.entity.LoanCustomer;
import com.cece.lms.request.inbound.SubscribeCustomerRequest;
import com.cece.lms.response.inbound.LMSResponse;
import com.cece.lms.service.LoanCustomerService;
import com.cece.lms.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanController {

    private final LoanService loanService;
    private final LoanCustomerService loanCustomerService;

    @PostMapping("/subscribe")
    public LMSResponse<LoanCustomer> subscribe(@RequestBody SubscribeCustomerRequest request) {
        try {
            LoanCustomer loanCustomer = loanCustomerService.subscribeCustomer(request.getCustomerNumber());
            return new LMSResponse<>(loanCustomer);
        }catch (Exception e){
            log.error("Error while subscribing customer", e);
            return new LMSResponse<>(e);
        }
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
