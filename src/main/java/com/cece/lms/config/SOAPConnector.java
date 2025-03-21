package com.cece.lms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.namespace.QName;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SOAPConnector extends WebServiceGatewaySupport {

    @Value("${api.core-banking.username}")
    private String username;
    @Value("${api.core-banking.password}")
    private String password;

    public Object callWebService(Object request) {
        return getWebServiceTemplate().marshalSendAndReceive(request,
                message -> {
                    try {
                        SoapMessage soapMessage = (SoapMessage) message;
                        soapMessage.setSoapAction("");

                        String auth = String.format("%s:%s",username, password);
                        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
                        String authHeader = "Basic " + new String(encodedAuth);
                        soapMessage.getSoapHeader().addHeaderElement(
                                        new QName("http://schemas.xmlsoap.org/ws/2002/12/secext", "Security"))
                                .setText(authHeader);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}

