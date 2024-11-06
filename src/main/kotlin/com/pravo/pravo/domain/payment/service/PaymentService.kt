package com.pravo.pravo.domain.payment.service

import com.pravo.pravo.domain.payment.model.PaymentLog
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository
import com.pravo.pravo.global.external.toss.PaymentClient
import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.util.logger
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService(
    private val paymentLogRepository: PaymentLogRepository,
    private val paymentClient: PaymentClient
) {

    val logger = logger()

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

    fun confirmOrder(
        confirmRequestDto: ConfirmRequestDto
    ) {
        val confirm = paymentClient.confirm(
            "Basic "+ Base64.getEncoder().encodeToString("test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6:".toByteArray()),
            confirmRequestDto
        )
        logger.info(confirm.toString())
        //TODO Response로 PaymentLog Update
        //TODO Front로 Reponse
    }
}