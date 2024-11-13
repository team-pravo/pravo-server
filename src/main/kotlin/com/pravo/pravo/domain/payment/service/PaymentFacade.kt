package com.pravo.pravo.domain.payment.service

import com.pravo.pravo.domain.payment.enums.PaymentStatus
import com.pravo.pravo.domain.payment.model.Card
import com.pravo.pravo.domain.payment.model.EasyPay
import com.pravo.pravo.domain.payment.model.PaymentLog
import com.pravo.pravo.global.external.toss.PaymentClient
import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.util.logger
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.UUID

@Service
class PaymentFacade(
    private val paymentClient: PaymentClient,
    private val paymentService: PaymentService,
) {
    val logger = logger()

    @Transactional
    fun requestOrder(memberId: Long): String {
        var id = UUID.randomUUID().toString()
        while (paymentService.existOrderId(id)) {
            id = UUID.randomUUID().toString()
        }

        val pendingPaymentLog = PaymentLog.getPendingPaymentLog(id)
        // Pending Promise 생성

        return paymentService.savePaymentLog(pendingPaymentLog).orderId
    }

    @Transactional
    fun confirmOrder(confirmRequestDto: ConfirmRequestDto) {
        val confirm =
            paymentClient.confirm(
                "Basic " +
                    Base64
                        .getEncoder()
                        .encodeToString("test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6:".toByteArray()),
                confirmRequestDto,
            )
        logger.info(confirm.toString())

        val paymentLog =
            paymentService.findPaymentLogByOrderId(confirmRequestDto.orderId)

        val card = confirm.card?.let { Card(it) }
        val easyPay = confirm.easyPay?.let { EasyPay(it) }

        paymentLog.updateFromConfirmResponse(confirm, card?.id, easyPay?.id, PaymentStatus.COMPLETED)
        paymentService.saveCardAndEasyPay(card, easyPay)

        // TODO Front로 Reponse Success 및 Error 확인
    }
}
