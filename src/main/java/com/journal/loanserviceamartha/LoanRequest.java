package com.journal.loanserviceamartha;

import java.math.BigDecimal;

record LoanRequest(String borrowerId, BigDecimal principal, BigDecimal rate, BigDecimal roi) {
}

