package com.pravo.pravo.domain.payment.controller

import com.pravo.pravo.domain.payment.service.PaymentService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment")
class PaymentController(
    private val paymentService: PaymentService
): PaymentApi {

    override fun requestOrder(
        @RequestParam memberId: Long
    ): Long {
        return paymentService.requestOrder(memberId)
    }
}