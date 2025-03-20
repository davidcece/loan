package com.cece.lms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SOAPConfig {

    @Value("${api.core-banking.kyc.url}")
    private String kycUrl;
    @Value("${api.core-banking.transactions.url}")
    private String transactionsUrl;

    @Bean
    public Jaxb2Marshaller kycMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.credable.soap.kyc");
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller transactionsMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.credable.soap.transactions");
        return marshaller;
    }

    @Bean
    public SOAPConnector kycSOAPConnector(Jaxb2Marshaller kycMarshaller) {
        SOAPConnector client = new SOAPConnector();
        client.setDefaultUri(kycUrl);
        client.setMarshaller(kycMarshaller);
        client.setUnmarshaller(kycMarshaller);
        return client;
    }

    @Bean
    public SOAPConnector transactionsSOAPConnector(Jaxb2Marshaller transactionsMarshaller) {
        SOAPConnector client = new SOAPConnector();
        client.setDefaultUri(transactionsUrl);
        client.setMarshaller(transactionsMarshaller);
        client.setUnmarshaller(transactionsMarshaller);
        return client;
    }
}

