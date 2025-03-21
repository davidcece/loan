package com.cece.lms.request.inbound;

import lombok.Data;

@Data
public class LoanRequest {
    private String customerNumber;
    private Double amount;
}
