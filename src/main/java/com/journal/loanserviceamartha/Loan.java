package com.journal.loanserviceamartha;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "loanId")
class Loan {
    String loanId;
    String borrowerId;
    BigDecimal principal;
    BigDecimal rate;
    BigDecimal roi;
    LoanState state;
    ApprovalInfo approvalInfo;
    List<Investor> investors;
    DisbursementInfo disbursementInfo;
    String agreementLink;

    Loan(String borrowerId, BigDecimal principal, BigDecimal rate, BigDecimal roi) {
        this.borrowerId = borrowerId;
        this.principal = principal;
        this.rate = rate;
        this.roi = roi;
        this.state = LoanState.PROPOSED;
        this.investors = new ArrayList<>();
    }

    void approve(String pictureProof, String validatorId, LocalDateTime approvalDate) {
        if (this.state != LoanState.PROPOSED) {
            throw new IllegalStateException("Loan can only be approved from the PROPOSED state.");
        }
        this.approvalInfo = new ApprovalInfo(pictureProof, validatorId, approvalDate);
        this.state = LoanState.APPROVED;
    }

    void addInvestor(String investorId, BigDecimal amount) {
        if (this.state != LoanState.APPROVED) {
            throw new IllegalStateException("Loan must be APPROVED to add investors.");
        }
        BigDecimal totalInvested = investors.stream().map(Investor::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalInvested.add(amount).compareTo(this.principal) > 0) {
            throw new IllegalArgumentException("Invested amount cannot exceed the loan principal.");
        }
        this.investors.add(new Investor(investorId, amount));
        if (totalInvested.add(amount).compareTo(this.principal) == 0) {
            this.state = LoanState.INVESTED;
        }
    }

    void disburse(String agreementLetter, String fieldOfficerId, LocalDateTime disbursementDate) {
        if (this.state != LoanState.INVESTED) {
            throw new IllegalStateException("Loan must be INVESTED to be disbursed.");
        }
        this.disbursementInfo = new DisbursementInfo(agreementLetter, fieldOfficerId, disbursementDate);
        this.state = LoanState.DISBURSED;
    }
}

record ApprovalInfo(String pictureProof, String validatorId, LocalDateTime approvalDate) {
}

record Investor(String investorId, BigDecimal amount) {
}

record DisbursementInfo(String agreementLetter, String fieldOfficerId, LocalDateTime disbursementDate) {
}
