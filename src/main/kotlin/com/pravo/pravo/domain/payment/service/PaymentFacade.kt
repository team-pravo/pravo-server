package com.pravo.pravo.domain.payment.service

import com.pravo.pravo.domain.member.service.MemberService
import com.pravo.pravo.domain.payment.dto.response.RequestOrderResponseDto
import com.pravo.pravo.domain.payment.enums.PaymentStatus
import com.pravo.pravo.domain.payment.model.Card
import com.pravo.pravo.domain.payment.model.EasyPay
import com.pravo.pravo.domain.payment.model.PaymentLog
import com.pravo.pravo.domain.promise.dto.request.PromiseCreateDto
import com.pravo.pravo.domain.promise.model.enums.RoleStatus
import com.pravo.pravo.domain.promise.service.PromiseRoleService
import com.pravo.pravo.domain.promise.service.PromiseService
import com.pravo.pravo.global.external.toss.PaymentClient
import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.util.logger
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.UUID

@Service
class PaymentFacade(
    private val paymentClient: PaymentClient,
    private val paymentService: PaymentService,
    private val promiseService: PromiseService,
    private val promiseRoleService: PromiseRoleService,
    private val memberService: MemberService,
    @Value("\${toss.secretKey}") private val tossSecretKey: String,
) {
    val logger = logger()

    fun createPaymentUUID(): String {
        var id = UUID.randomUUID().toString()
        while (paymentService.existOrderId(id)) {
            id = UUID.randomUUID().toString()
        }
        return id
    }

    @Transactional
    fun requestOrder(
        memberId: Long,
        promiseCreateDto: PromiseCreateDto,
    ): RequestOrderResponseDto {
        val id = createPaymentUUID()
        val pendingPromise = promiseService.createPendingPromise(promiseCreateDto)
        val pendingPaymentLog = PaymentLog.getPendingPaymentLog(id, memberId, pendingPromise.id)
        val member = memberService.getMemberById(memberId)
        promiseRoleService.createPendingPromiseRole(member, pendingPromise, RoleStatus.ORGANIZER)

        return RequestOrderResponseDto.of(
            paymentService.savePaymentLog(pendingPaymentLog).orderId,
            pendingPromise.id,
        )
    }

    @Transactional
    fun requestOrderParticipant(
        memberId: Long,
        promiseId: Long,
    ): RequestOrderResponseDto {
        val id = createPaymentUUID()
        val pendingPaymentLog = PaymentLog.getPendingPaymentLog(id, memberId, promiseId)
        return RequestOrderResponseDto.of(
            paymentService.savePaymentLog(pendingPaymentLog).orderId,
            promiseId,
        )
    }

    @Transactional
    fun confirmOrder(confirmRequestDto: ConfirmRequestDto) {
        val confirm =
            paymentClient.confirm(
                "Basic " +
                    Base64
                        .getEncoder()
                        .encodeToString(tossSecretKey.toByteArray()),
                confirmRequestDto,
            )
        logger.info(confirm.toString())

        val paymentLog =
            paymentService.findPaymentLogByOrderId(confirmRequestDto.orderId)

        val card = confirm.card?.let { Card(it) }
        val easyPay = confirm.easyPay?.let { EasyPay(it) }

        paymentLog.updateFromConfirmResponse(
            confirm,
            card?.id,
            easyPay?.id,
            PaymentStatus.COMPLETED,
        )
        paymentService.saveCardAndEasyPay(card, easyPay)
    }
}
