package com.journal.loanserviceamartha;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
class LoanService {
    private final Map<String, Loan> loans = new HashMap<>();
    private int loanIdCounter = 1;

    Loan createLoan(LoanRequest request) {
        String loanId = "loan" + (loanIdCounter++);
        var loan = new Loan(request.borrowerId(), request.principal(), request.rate(), request.roi());
        loan.setLoanId(loanId);
        loans.put(loanId, loan);
        return loan;
    }

    Loan approveLoan(String loanId, ApprovalRequest request) {
        var loan = loans.get(loanId);

        if (null == loan) {
            throw new IllegalArgumentException("Loan with id " + loanId + " not found");
        }

        loan.approve(request.pictureProof(), request.validatorId(), request.approvalDate());
        return loan;
    }

    Loan addInvestor(String loanId, InvestorRequest request) {
        var loan = loans.get(loanId);

        if (null == loan) {
            throw new IllegalArgumentException("Loan with id " + loanId + " not found");
        }

        loan.addInvestor(request.investorId(), request.amount());
        return loan;
    }

    Loan disburseLoan(String loanId, DisbursementRequest request) {
        var loan = loans.get(loanId);

        if (null == loan) {
            throw new IllegalArgumentException("Loan with id " + loanId + " not found");
        }

        loan.disburse(request.agreementLetter(), request.fieldOfficerId(), request.disbursementDate());
        return loan;
    }

    Loan getLoanDetails(String loanId) {
        var loan = loans.get(loanId);

        if (null == loan) {
            throw new IllegalArgumentException("Loan with id " + loanId + " not found");
        }

        return loan;
    }
}
