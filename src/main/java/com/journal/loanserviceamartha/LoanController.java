package com.journal.loanserviceamartha;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
class LoanController {

    private final LoanService loanService;

    @PostMapping
    Loan createLoan(@RequestBody LoanRequest request) {
        return loanService.createLoan(request);
    }

    @PostMapping("/{loanId}/approve")
    Loan approveLoan(@PathVariable String loanId, @RequestBody ApprovalRequest request) {
        return loanService.approveLoan(loanId, request);
    }

    @PostMapping("/{loanId}/invest")
    Loan addInvestor(@PathVariable String loanId, @RequestBody InvestorRequest request) {
        return loanService.addInvestor(loanId, request);
    }

    @PostMapping("/{loanId}/disburse")
    Loan disburseLoan(@PathVariable String loanId, @RequestBody DisbursementRequest request) {
        return loanService.disburseLoan(loanId, request);
    }

    @GetMapping("/{loanId}")
    Loan getLoanDetails(@PathVariable String loanId) {
        return loanService.getLoanDetails(loanId);
    }
}
