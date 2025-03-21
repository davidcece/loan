package com.cece.lms.service;

import com.cece.lms.entity.Loan;
import com.cece.lms.entity.LoanCustomer;
import com.cece.lms.entity.ScoringApplication;
import com.cece.lms.entity.enums.LoanStatus;
import com.cece.lms.repository.LoanRepository;
import com.cece.lms.response.outbound.ScoringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanCustomerService loanCustomerService;
    private final ScoringService scoringService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Loan requestLoan(String customerNumber, Double amount) {
        LoanCustomer customer = loanCustomerService.getCustomer(customerNumber);
        this.validateInitiatedLoan(customerNumber);

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setAmount(amount);
        loan.setStatus(LoanStatus.INITIATED);
        loan.setCreatedAt(LocalDateTime.now());
        loan.setDescription("Loan request initiated for customer");
        final Loan createdLoan = loanRepository.save(loan);

        executorService.submit(()->{
            String scoringToken = scoringService.initScoring(createdLoan.getCustomer().getCustomerNumber());
            Optional<ScoringResponse> scoringResponse =  scoringService.queryScoreStatus(scoringToken);
            if(scoringResponse.isPresent()){
                ScoringResponse score = scoringResponse.get();
                if(score.getLimitAmount() < createdLoan.getAmount()){
                    createdLoan.setStatus(LoanStatus.REJECTED);
                    createdLoan.setDescription("Loan request rejected due to low scoring of: "+score.getLimitAmount());
                }else {
                    createdLoan.setStatus(LoanStatus.APPROVED);
                    createdLoan.setDescription("Loan request approved with scoring of: "+score.getLimitAmount());
                }
            }else{
                createdLoan.setStatus(LoanStatus.FAILED);
                createdLoan.setDescription("Score for customer unavailable, Loan request failed.");
            }
            loanRepository.save(createdLoan);
        });

        return createdLoan;
    }



    public Loan getLoanStatus(String customerNumber) {
        Loan loan = loanRepository.findLastLoanStatus(customerNumber);
        if (loan == null) {
            throw new RuntimeException("Loan not found.");
        }
        return loan;
    }


    private void validateInitiatedLoan(String customerNumber) {
        Loan existingLoan = loanRepository.findByCustomerNumberAndStatus(customerNumber, LoanStatus.INITIATED);
        if (existingLoan != null) {
            throw new RuntimeException("Active loan already exists.");
        }
    }


}

