package com.pravo.pravo.global.external.toss

import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.external.toss.dto.response.ConfirmResponseDto
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "paymentClient", url = "https://api.tosspayments.com/v1/payments/confirm")
interface PaymentClient {
    @Headers("Content-Type: application/json")
    @PostMapping
    fun confirm(
        @RequestHeader("Authorization") token: String,
        @RequestBody confirmRequestDto: ConfirmRequestDto,
    ): ConfirmResponseDto
}
