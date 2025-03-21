package com.cece.lms.controller;

import com.cece.lms.entity.Loan;
import com.cece.lms.entity.LoanCustomer;
import com.cece.lms.request.inbound.LoanRequest;
import com.cece.lms.request.inbound.SubscribeCustomerRequest;
import com.cece.lms.response.inbound.LMSResponse;
import com.cece.lms.response.inbound.LoanResponse;
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
        } catch (Exception e) {
            log.error("Error while subscribing customer", e);
            return new LMSResponse<>(e);
        }
    }

    @PostMapping("/request")
    public LMSResponse<LoanResponse> requestLoan(@RequestBody LoanRequest request) {
        try {
            log.info("Requesting new loan: {}", request);
            Loan loan = loanService.requestLoan(request.getCustomerNumber(), request.getAmount());
            LoanResponse loanResponse = LoanResponse.builder()
                    .id(loan.getId())
                    .amount(loan.getAmount())
                    .createdAt(loan.getCreatedAt())
                    .description(loan.getDescription())
                    .status(loan.getStatus())
                    .customerNumber(loan.getCustomer().getCustomerNumber())
                    .build();
            return new LMSResponse<>(loanResponse);
        } catch (Exception e) {
            log.error("Error while requesting loan", e);
            return new LMSResponse<>(e);
        }

    }

    @GetMapping("/status/{customerNumber}")
    public LMSResponse<LoanResponse> getLoanStatus(@PathVariable String customerNumber) {
        try {
            log.info("Getting loan status for customer: {}", customerNumber);
            Loan loan = loanService.getLoanStatus(customerNumber);
            LoanResponse loanResponse = LoanResponse.builder()
                    .id(loan.getId())
                    .amount(loan.getAmount())
                    .createdAt(loan.getCreatedAt())
                    .description(loan.getDescription())
                    .status(loan.getStatus())
                    .customerNumber(loan.getCustomer().getCustomerNumber())
                    .build();
            return new LMSResponse<>(loanResponse);
        } catch (Exception e) {
            log.error("Error while getting loan status", e);
            return new LMSResponse<>(e);
        }

    }
}