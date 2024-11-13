package com.pravo.pravo.domain.payment.service

import com.pravo.pravo.domain.payment.model.Card
import com.pravo.pravo.domain.payment.model.EasyPay
import com.pravo.pravo.domain.payment.model.PaymentLog
import com.pravo.pravo.domain.payment.repository.CardRepository
import com.pravo.pravo.domain.payment.repository.EasyPayRepository
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentLogRepository: PaymentLogRepository,
    private val cardRepository: CardRepository,
    private val easyPayRepository: EasyPayRepository,
) {
    fun existOrderId(orderId: String): Boolean = paymentLogRepository.existsById(orderId)

    fun savePaymentLog(paymentLog: PaymentLog): PaymentLog = paymentLogRepository.save(paymentLog)

    fun findPaymentLogByOrderId(orderId: String): PaymentLog =
        paymentLogRepository.findById(orderId).orElseThrow {
            NotFoundException(ErrorCode.NOT_FOUND)
        }

    fun saveCardAndEasyPay(
        card: Card?,
        easyPay: EasyPay?,
    ) {
        card?.let { cardRepository.save(it) }
        easyPay?.let { easyPayRepository.save(it) }
    }
}
