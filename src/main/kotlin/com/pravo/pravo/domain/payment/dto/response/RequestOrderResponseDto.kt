package com.pravo.pravo.domain.payment.dto.response

data class RequestOrderResponseDto(
    val orderId: String,
) {
    companion object {
        fun of(orderId: String): RequestOrderResponseDto = of(orderId)
    }
}
