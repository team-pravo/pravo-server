package com.pravo.pravo.global.external.toss.dto.response

data class CashReceiptHistoryDto(
    val receiptKey: String,
    val orderId: String,
    val orderName: String,
    val type: String,
    val issueNumber: String,
    val receiptUrl: String,
    val businessNumber: String,
    val transactionType: String,
    val amount: Int,
    val taxFreeAmount: Int,
    val issueStatus: String,
    val failure: FailureDto,
    val customerIdentityNumber: String,
    val requestedAt: String,
)
