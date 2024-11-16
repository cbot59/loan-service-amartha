package com.journal.loanserviceamartha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LoanServiceTest {

    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanService = new LoanService();
    }

    @Nested
    class CreateLoan {
        @Test
        void shouldReturnLoanProposed() {
            var actual = loanService.createLoan(new LoanRequest(
                "borrower-1",
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(102000)
            ));

            assertThat(actual).satisfies(loan -> {
                assertThat(loan.getLoanId()).isEqualTo("loan1");
                assertThat(loan.getState()).isEqualTo(LoanState.PROPOSED);
                assertThat(loan.getBorrowerId()).isEqualTo("borrower-1");
                assertThat(loan.getPrincipal()).isEqualTo(BigDecimal.valueOf(100000));
                assertThat(loan.getRate()).isEqualTo(BigDecimal.valueOf(2));
                assertThat(loan.getRoi()).isEqualTo(BigDecimal.valueOf(102000));
                assertThat(loan.getInvestors().size()).isEqualTo(0);
            });
        }
    }

    @Nested
    class ApproveLoan {
        @Test
        void shouldThrowExceptionWhenLoanNotExist() {
            assertThatThrownBy(() -> loanService.approveLoan("loanloan", new ApprovalRequest(
                "pictureProof",
                "validator-1",
                LocalDateTime.of(2025, 1, 1, 0, 0, 0)
            )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan with id loanloan not found");
        }

        @Test
        void shouldReturnLoanApproved() {
            loanService.createLoan(new LoanRequest(
                "borrower-1",
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(102000)
            ));

            var actual = loanService.approveLoan("loan1", new ApprovalRequest(
                "pictureProof",
                "validator-1",
                LocalDateTime.of(2025, 1, 1, 0, 0, 0)
            ));

            assertThat(actual).satisfies(loan -> {
                    assertThat(loan.getLoanId()).isEqualTo("loan1");
                    assertThat(loan.getState()).isEqualTo(LoanState.APPROVED);
                    assertThat(loan.getApprovalInfo()).isNotNull();
                }
            );
        }
    }

    @Nested
    class AddInvestor {
        @Test
        void shouldThrowExceptionWhenLoanNotExist() {
            assertThatThrownBy(() -> loanService.addInvestor("loanloan", new InvestorRequest(
                "investor-1",
                BigDecimal.ZERO
            )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan with id loanloan not found");
        }

        @Test
        void shouldThrowExceptionWhenLoanNotApproved() {
            createInitialLoan();

            assertThatThrownBy(() -> loanService.addInvestor("loan1", new InvestorRequest(
                "investor-1",
                BigDecimal.ZERO
            )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Loan must be APPROVED to add investors.");
        }

        @Test
        void shouldThrowExceptionWhenInvestmentGreaterThanPrincipal() {
            createInitialLoan();
            approveInitialLoan();

            assertThatThrownBy(() -> loanService.addInvestor("loan1", new InvestorRequest(
                "investor-1",
                BigDecimal.valueOf(200000)
            )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invested amount cannot exceed the loan principal.");
        }

        @Test
        void shouldReturnLoanApprovedWhenInvestRequestLessThanPrincipal() {
            createInitialLoan();
            approveInitialLoan();

            var actual = loanService.addInvestor("loan1", new InvestorRequest(
                "investor-1",
                BigDecimal.valueOf(50000)
            ));

            assertThat(actual).satisfies(loan -> {
                    assertThat(loan.getLoanId()).isEqualTo("loan1");
                    assertThat(loan.getState()).isEqualTo(LoanState.APPROVED);
                    assertThat(loan.getInvestors().size()).isEqualTo(1);
                }
            );
        }

        @Test
        void shouldReturnLoanInvestedWhenInvestRequestEqualsPrincipal() {
            createInitialLoan();
            approveInitialLoan();

            var actual = loanService.addInvestor("loan1", new InvestorRequest(
                "investor-1",
                BigDecimal.valueOf(100000)
            ));

            assertThat(actual).satisfies(loan -> {
                    assertThat(loan.getLoanId()).isEqualTo("loan1");
                    assertThat(loan.getState()).isEqualTo(LoanState.INVESTED);
                    assertThat(loan.getInvestors().size()).isEqualTo(1);
                }
            );
        }

        private void approveInitialLoan() {
            loanService.approveLoan("loan1", new ApprovalRequest(
                "pictureProof",
                "validator-1",
                LocalDateTime.of(2025, 1, 1, 0, 0, 0)
            ));
        }

        private void createInitialLoan() {
            loanService.createLoan(new LoanRequest(
                "borrower-1",
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(102000)
            ));
        }
    }

    @Nested
    class DisburseLoan {
        @Test
        void shouldThrowExceptionWhenLoanNotExist() {
            assertThatThrownBy(() -> loanService.disburseLoan("loanloan", new DisbursementRequest(
                "agreementLetter",
                "officer-1",
                LocalDateTime.of(2025, 1, 2, 0, 0, 0)
            )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan with id loanloan not found");
        }

        @Test
        void shouldThrowExceptionWhenLoanNotInvested() {
            createInitialLoan();

            assertThatThrownBy(() -> loanService.disburseLoan("loan1", new DisbursementRequest(
                "agreementLetter",
                "officer-1",
                LocalDateTime.of(2025, 1, 2, 0, 0, 0)
            )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Loan must be INVESTED to be disbursed.");
        }

        @Test
        void shouldReturnLoanDisbursed() {
            createInitialLoan();
            approveInitialLoan();
            investInitialLoan();

            var actual = loanService.disburseLoan("loan1", new DisbursementRequest(
                "agreementLetter",
                "officer-1",
                LocalDateTime.of(2025, 1, 2, 0, 0, 0)
            ));

            assertThat(actual).satisfies(loan -> {
                    assertThat(loan.getLoanId()).isEqualTo("loan1");
                    assertThat(loan.getState()).isEqualTo(LoanState.DISBURSED);
                    assertThat(loan.getDisbursementInfo()).isNotNull();
                }
            );
        }

        private void investInitialLoan() {
            loanService.addInvestor("loan1", new InvestorRequest(
                "investor-1",
                BigDecimal.valueOf(100000)
            ));
        }

        private void approveInitialLoan() {
            loanService.approveLoan("loan1", new ApprovalRequest(
                "pictureProof",
                "validator-1",
                LocalDateTime.of(2025, 1, 1, 0, 0, 0)
            ));
        }

        private void createInitialLoan() {
            loanService.createLoan(new LoanRequest(
                "borrower-1",
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(102000)
            ));
        }
    }

    @Nested
    class GetLoanDetails {
        @Test
        void shouldThrowExceptionWhenLoanNotFound() {
            assertThatThrownBy(() -> loanService.getLoanDetails("loanloan"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan with id loanloan not found");
        }

        @Test
        void shouldReturnLoanDetails() {
            createInitialLoan();

            assertThat(loanService.getLoanDetails("loan1")).isNotNull();
        }

        private void createInitialLoan() {
            loanService.createLoan(new LoanRequest(
                "borrower-1",
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(102000)
            ));
        }
    }
}
