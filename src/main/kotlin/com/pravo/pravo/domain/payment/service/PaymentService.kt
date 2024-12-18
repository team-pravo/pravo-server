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
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${toss.secretKey}") private val tossSecretKey: String,
) {
    val logger = logger()

    fun existOrderId(orderId: String): Boolean = paymentLogRepository.existsById(orderId)

    fun savePaymentLog(paymentLog: PaymentLog): PaymentLog = paymentLogRepository.save(paymentLog)

    fun findPaymentLogByOrderId(orderId: String): PaymentLog =
        paymentLogRepository.findById(orderId).orElseThrow {
            NotFoundException(ErrorCode.NOT_FOUND)
        }

    fun findByMemberIdAndPromiseId(
        memberId: Long,
        promiseId: Long,
    ): PaymentLog =
        paymentLogRepository.findByMemberIdAndPromiseId(memberId, promiseId).orElseThrow {
            NotFoundException(ErrorCode.NOT_FOUND, "결제 정보를 찾을 수 없습니다")
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

    @Transactional
    fun cancelPayment(
        promiseId: Long,
        memberId: Long,
    ) {
        val paymentLog =
            paymentLogRepository.findByMemberIdAndPromiseId(memberId, promiseId).orElseThrow {
                NotFoundException(ErrorCode.NOT_FOUND, "결제 정보를 찾을 수 없습니다")
            }

        val idempotencyKey = getOrCreateIdempotencyKey(promiseId, memberId)

        val cancel =
            paymentClient.cancel(
                "Basic " +
                    Base64
                        .getEncoder()
                        .encodeToString(tossSecretKey.toByteArray()),
                idempotencyKey,
                paymentLog.paymentKey,
                CancelRequestDto("약속 정산"),
            )
        logger.info(cancel.toString())

        paymentLog.setPaymentStatus(PaymentStatus.CANCELED)
    }
}
