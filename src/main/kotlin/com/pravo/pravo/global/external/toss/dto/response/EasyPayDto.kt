package com.pravo.pravo.global.external.toss.dto.response

data class EasyPayDto(
    val provider: String,
    val amount: Int,
    val discountAmount: Int,
)
