package com.pravo.pravo.global.external.toss

import com.pravo.pravo.global.external.toss.dto.request.CancelRequestDto
import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.external.toss.dto.response.TossResponseDto
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "paymentClient", url = "https://api.tosspayments.com/v1/payments")
interface PaymentClient {
    @Headers("Content-Type: application/json")
    @PostMapping("/confirm")
    fun confirm(
        @RequestHeader("Authorization") token: String,
        @RequestBody confirmRequestDto: ConfirmRequestDto,
    ): TossResponseDto

    @Headers("Content-Type: application/json")
    @PostMapping("/{paymentKey}/cancel")
    fun cancel(
        @RequestHeader("Authorization") token: String,
        @RequestHeader("Idempotency-Key") idempotencyKey: String,
        @PathVariable paymentKey: String,
        @RequestBody cancelRequestDto: CancelRequestDto,
    ): TossResponseDto
}
