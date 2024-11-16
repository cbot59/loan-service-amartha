package com.journal.loanserviceamartha;

import java.time.LocalDateTime;

record DisbursementRequest(String agreementLetter, String fieldOfficerId, LocalDateTime disbursementDate) {
}
