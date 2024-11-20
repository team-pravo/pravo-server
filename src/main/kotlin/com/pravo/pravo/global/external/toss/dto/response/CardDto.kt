package com.pravo.pravo.global.external.toss.dto.response

data class CardDto(
    val issuerCode: String,
    val acquirerCode: String?,
    val number: String,
    val installmentPlanMonths: Int,
    val isInterestFree: Boolean,
    val interestPayer: String?,
    val approveNo: String,
    val useCardPoint: Boolean,
    val cardType: String,
    val ownerType: String,
    val acquireStatus: String,
    val amount: Int,
)
