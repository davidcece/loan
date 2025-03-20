package com.cece.lms.service;

import com.cece.lms.config.SOAPConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.credable.soap.kyc.CustomerResponse;
import com.credable.soap.kyc.CustomerRequest;
import com.credable.soap.transactions.TransactionsRequest;
import com.credable.soap.transactions.TransactionsResponse;

@Service
public class CoreBankingIntegrationService {

    private final SOAPConnector kycSOAPConnector;
    private final SOAPConnector transactionsSOAPConnector;

    public CoreBankingIntegrationService(@Qualifier("kycSOAPConnector") SOAPConnector kycSOAPConnector,
                                         @Qualifier("transactionsSOAPConnector") SOAPConnector transactionsSOAPConnector) {
        this.kycSOAPConnector = kycSOAPConnector;
        this.transactionsSOAPConnector = transactionsSOAPConnector;
    }


    public CustomerResponse getCustomerKYC(String customerNumber) {
        CustomerRequest request = new CustomerRequest();
        request.setCustomerNumber(customerNumber);

        return (CustomerResponse) kycSOAPConnector.callWebService(request);
    }


    public TransactionsResponse getTransactionData(String customerNumber) {
        TransactionsRequest request = new TransactionsRequest();
        request.setCustomerNumber(customerNumber);

        return (TransactionsResponse) transactionsSOAPConnector.callWebService(request);
    }


}
