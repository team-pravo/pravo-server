package com.pravo.pravo.domain.payment.controller

import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.domain.payment.service.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment")
class PaymentController(
    private val paymentService: PaymentService
): PaymentApi {

    @GetMapping
    override fun requestOrder(
        @RequestParam memberId: Long
    ): String {
        return paymentService.requestOrder(memberId)
    }

    @PostMapping
    override fun confirmOrder(
        @RequestBody confirmRequestDto: ConfirmRequestDto
    ) {
        return paymentService.confirmOrder(confirmRequestDto)
    }
}