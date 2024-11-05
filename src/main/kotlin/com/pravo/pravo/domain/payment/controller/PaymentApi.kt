package com.pravo.pravo.domain.payment.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Payment", description = "결제 API")
interface PaymentApi {

    @Operation(summary = "결제 요청", description = "약속, 결제 정보를 생성 후 paymentId를 반환합니다.")
    fun requestOrder(
        memberId: Long
    ): String

    @Operation(summary = "결제 승인", description = "paymentKey에 해당하는 결제를 검증하고 승인합니다.")
    fun confirmOrder(
        confirmRequestDto: confirmRequestDto
    )
}