package com.cece.lms.entity;

import com.cece.lms.entity.enums.LoanStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerNumber;
    private Double amount;
    private LocalDateTime createdAt;
    private String scoringToken;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;


}
