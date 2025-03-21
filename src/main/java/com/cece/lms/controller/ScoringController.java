package com.cece.lms.controller;

import com.cece.lms.service.CoreBankingIntegrationService;
import com.credable.soap.transactions.TransactionData;
import com.credable.soap.transactions.TransactionsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scoring")
@RequiredArgsConstructor
@Slf4j
public class ScoringController {

    private final CoreBankingIntegrationService coreBankingIntegrationService;

    @GetMapping("/transactions")
    public List<TransactionData> getTransactionHistory(@RequestParam String customerNumber) {
        log.info("Getting transaction history for customer {}", customerNumber);
        TransactionsResponse transactionData = coreBankingIntegrationService.getTransactionData(customerNumber);
        return transactionData.getTransactions();
    }
}
