package com.cece.lms.service;

import com.cece.lms.entity.LoanCustomer;
import com.cece.lms.repository.LoanCustomerRepository;
import com.credable.soap.kyc.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanCustomerService {
    private final LoanCustomerRepository loanCustomerRepository;
    private final CoreBankingIntegrationService coreBankingIntegrationService;

    public LoanCustomer subscribeCustomer(String customerNumber) {
        log.info("Subscribing customer with number: {}", customerNumber);

        LoanCustomer existingCustomer = loanCustomerRepository.findByCustomerNumber(customerNumber);
        if (existingCustomer != null) {
            throw new RuntimeException("Customer already exists.");
        }

        CustomerResponse customerKYC = coreBankingIntegrationService.getCustomerKYC(customerNumber);
        if(customerKYC==null || customerKYC.getCustomer()==null){
            throw new RuntimeException("CBS Customer not found.");
        }

        LoanCustomer loanCustomer = new LoanCustomer(customerKYC.getCustomer());
        LoanCustomer createdCustomer = loanCustomerRepository.save(loanCustomer);
        log.info("Customer subscribed successfully: {}", createdCustomer);
        return createdCustomer;
    }
}
