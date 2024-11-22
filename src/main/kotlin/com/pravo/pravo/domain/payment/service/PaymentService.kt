@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.pravo.pravo.domain.payment.service

import com.pravo.pravo.domain.payment.enums.PaymentStatus
import com.pravo.pravo.domain.payment.model.Card
import com.pravo.pravo.domain.payment.model.EasyPay
import com.pravo.pravo.domain.payment.model.PaymentLog
import com.pravo.pravo.domain.payment.repository.CardRepository
import com.pravo.pravo.domain.payment.repository.EasyPayRepository
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.NotFoundException
import com.pravo.pravo.global.external.toss.PaymentClient
import com.pravo.pravo.global.external.toss.dto.request.CancelRequestDto
import com.pravo.pravo.global.util.logger
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class PaymentService(
    private val paymentLogRepository: PaymentLogRepository,
    private val cardRepository: CardRepository,
    private val easyPayRepository: EasyPayRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val paymentClient: PaymentClient,
) {
    val logger = logger()

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

    fun getOrCreateIdempotencyKey(
        promiseId: Long,
        memberId: Long,
    ): String {
        val redisKey = "idempotency:$promiseId:$memberId"

        val existingKey = redisTemplate.opsForValue().get(redisKey)
        if (existingKey != null) {
            return existingKey
        }

        val newKey = UUID.randomUUID().toString()

        redisTemplate.opsForValue().set(redisKey, newKey, 24, TimeUnit.HOURS)

        return newKey
    }

    // TODO 약속 정산 부분에서 호출
    fun cancelPayment(
        promiseId: Long,
        memberId: Long,
    ) {
        val paymentLog =
            paymentLogRepository.findByMemberIdAndPromiseId(memberId, promiseId).orElseThrow {
                NotFoundException(ErrorCode.NOT_FOUND)
            }

        val idempotencyKey = getOrCreateIdempotencyKey(promiseId, memberId)

        val cancel =
            paymentClient.cancel(
                "Basic " +
                    Base64
                        .getEncoder()
                        .encodeToString("test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6:".toByteArray()),
                idempotencyKey,
                paymentLog.paymentKey,
                CancelRequestDto("약속 정산"),
            )
        logger.info(cancel.toString())

        paymentLog.setPaymentStatus(PaymentStatus.CANCELED)
    }
}
