package com.pravo.pravo.domain.payment.service

import com.pravo.pravo.domain.payment.model.PaymentLog
import org.springframework.stereotype.Service

@Service
class PaymentService {

    fun requestOrder(
        memberId: Long
    ): Long {
        val pendingPaymentLog = PaymentLog.getPendingPaymentLog()
        // Pending Promise 생성

        return pendingPaymentLog.id
    }
}