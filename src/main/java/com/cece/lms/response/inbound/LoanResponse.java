package com.cece.lms.response.inbound;

import com.cece.lms.entity.enums.LoanStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoanResponse {
    private Long id;
    private Double amount;
    private LocalDateTime createdAt;
    private String description;
    private LoanStatus status;
    private String customerNumber;
}
