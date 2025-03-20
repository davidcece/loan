package com.cece.lms.controller;

import com.cece.lms.service.CoreBankingIntegrationService;
import com.credable.soap.transactions.TransactionsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring")
@RequiredArgsConstructor
@Slf4j
public class ScoringController {

    private final CoreBankingIntegrationService coreBankingIntegrationService;

    @PostMapping("/transactions")
    public TransactionsResponse getTransactionHistory(@RequestParam String customerNumber) {
        log.info("Getting transaction history for customer {}", customerNumber);
        return coreBankingIntegrationService.getTransactionData(customerNumber);
    }
}
