package com.pravo.pravo.global.external.toss.dto.request

data class ConfirmRequestDto(
    val paymentKey: String,
    val orderId: String,
    val amount: Long
)
