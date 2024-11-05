package com.pravo.pravo.domain.payment.service

import ch.qos.logback.core.util.StringUtil
import com.pravo.pravo.domain.payment.model.PaymentLog
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PaymentService (
  private val paymentLogRepository: PaymentLogRepository
) {

    fun requestOrder(
        memberId: Long
    ): String {
        var id = UUID.randomUUID().toString()
        while (paymentLogRepository.existsById(id)) {
            id = UUID.randomUUID().toString()
        }

        val pendingPaymentLog = PaymentLog.getPendingPaymentLog(id)
        // Pending Promise 생성

        return pendingPaymentLog.paymentId
    }
}