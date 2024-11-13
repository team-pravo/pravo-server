package com.pravo.pravo.domain.payment.service

import com.pravo.pravo.domain.payment.repository.PaymentLogRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentLogRepository: PaymentLogRepository,
) {
    fun existOrderId(orderId: String): Boolean = paymentLogRepository.existsById(orderId)
}
