package com.pravo.pravo.global.external.toss.dto.response

data class CancelDto(
    val cancelAmount: Double,
    val cancelReason: String,
    val taxFreeAmount: Double,
    val taxExemptionAmount: Int,
    val refundableAmount: Double,
    val easyPayDiscountAmount: Double,
    val canceledAt: String,
    val transactionKey: String,
    val receiptKey: String?,
    val cancelStatus: String,
    val cancelRequestId: String?,
)
