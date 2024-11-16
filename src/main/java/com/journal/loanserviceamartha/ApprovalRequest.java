package com.journal.loanserviceamartha;

import java.time.LocalDateTime;

record ApprovalRequest(String pictureProof, String validatorId, LocalDateTime approvalDate) {
}
