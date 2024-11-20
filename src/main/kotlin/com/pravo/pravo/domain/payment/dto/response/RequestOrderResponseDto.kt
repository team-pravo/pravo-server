package com.pravo.pravo.domain.payment.dto.response

data class RequestOrderResponseDto(
    val orderId: String,
    val promiseId: Long,
) {
    companion object {
        fun of(
            orderId: String,
            promiseId: Long,
        ): RequestOrderResponseDto = of(orderId, promiseId)
    }
}
