package com.cece.lms.entity;

import com.cece.lms.entity.enums.LoanStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private LocalDateTime createdAt;
    private String scoreToken;
    private String description;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private LoanCustomer customer;
}
