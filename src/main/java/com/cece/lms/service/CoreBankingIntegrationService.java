package com.cece.lms.service;

import com.cece.lms.config.SOAPConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.credable.soap.kyc.CustomerResponse;
import com.credable.soap.kyc.CustomerRequest;
import com.credable.soap.transactions.TransactionsRequest;
import com.credable.soap.transactions.TransactionsResponse;

@Service
@Slf4j
public class CoreBankingIntegrationService {

    private final SOAPConnector kycSOAPConnector;
    private final SOAPConnector transactionsSOAPConnector;

    public CoreBankingIntegrationService(@Qualifier("kycSOAPConnector") SOAPConnector kycSOAPConnector,
                                         @Qualifier("transactionsSOAPConnector") SOAPConnector transactionsSOAPConnector) {
        this.kycSOAPConnector = kycSOAPConnector;
        this.transactionsSOAPConnector = transactionsSOAPConnector;
        log.info("CoreBankingIntegrationService initialized with KYC and Transactions SOAP connectors");
    }

    public CustomerResponse getCustomerKYC(String customerNumber) {
        log.info("Fetching KYC data for customer number: {}", customerNumber);
        CustomerRequest request = new CustomerRequest();
        request.setCustomerNumber(customerNumber);

        CustomerResponse response = (CustomerResponse) kycSOAPConnector.callWebService(request);
        log.info("Received KYC response for customer number: {}", customerNumber);
        return response;
    }

    public TransactionsResponse getTransactionData(String customerNumber) {
        log.info("Fetching transaction data for customer number: {}", customerNumber);
        TransactionsRequest request = new TransactionsRequest();
        request.setCustomerNumber(customerNumber);

        TransactionsResponse response = (TransactionsResponse) transactionsSOAPConnector.callWebService(request);
        log.info("Received transaction data for customer number: {}", customerNumber);
        return response;
    }
}