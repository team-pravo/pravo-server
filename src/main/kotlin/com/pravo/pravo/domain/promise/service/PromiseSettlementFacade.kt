package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.fine.service.FineLogService
import com.pravo.pravo.domain.member.model.Member
import com.pravo.pravo.domain.payment.enums.PaymentStatus
import com.pravo.pravo.domain.payment.service.PaymentService
import com.pravo.pravo.domain.point.model.PointLogStatus
import com.pravo.pravo.domain.point.service.PointLogService
import com.pravo.pravo.domain.promise.dto.request.PromiseSettlementRequestDto
import com.pravo.pravo.domain.promise.dto.response.PromiseSettlementResponseDto
import com.pravo.pravo.domain.promise.model.Promise
import com.pravo.pravo.domain.promise.model.PromiseRole
import com.pravo.pravo.domain.promise.model.enums.ParticipantStatus
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus
import com.pravo.pravo.domain.promise.model.enums.RoleStatus
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.BadRequestException
import com.pravo.pravo.global.error.exception.BaseException
import com.pravo.pravo.global.error.exception.UnauthorizedException
import com.pravo.pravo.global.util.logger
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PromiseSettlementFacade(
    private val promiseService: PromiseService,
    private val promiseRoleService: PromiseRoleService,
    private val pointLogService: PointLogService,
    private val paymentService: PaymentService,
    private val fineLogService: FineLogService,
) {
    val log = logger()

    @Transactional
    fun settlePromise(
        memberId: Long,
        settlementRequest: PromiseSettlementRequestDto,
    ): PromiseSettlementResponseDto {
        val promise = promiseService.getPromise(settlementRequest.promiseId)
        val participants = validateAndGetParticipants(promise, memberId)

        val (attendees, absentees) =
            updateAndSplitParticipants(
                participants = participants,
                attendedMemberIds = settlementRequest.memberIds,
            )
        promise.updateStatus(PromiseStatus.COMPLETED)
        return if (attendees.isEmpty()) {
            processEmptyAttendees(absentees, promise)
        } else {
            processSettlement(attendees, absentees, promise)
        }
    }

    private fun processEmptyAttendees(
        absentees: List<PromiseRole>,
        promise: Promise,
    ): PromiseSettlementResponseDto {
        absentees.forEach {
            it.updateStatus(ParticipantStatus.NOT_ATTENDED)
            fineLogService.saveFineLog(promise.deposit.toLong(), it.member.id, promise.id)
        }

        return PromiseSettlementResponseDto(
            promise.id,
            promise.name,
            promise.deposit,
            0L,
            0,
            absentees.size,
        )
    }

    private fun processSettlement(
        attendees: List<PromiseRole>,
        absentees: List<PromiseRole>,
        promise: Promise,
    ): PromiseSettlementResponseDto {
        val earnedPoint = calculateEarnedPoint(attendees, absentees, promise.deposit)

        processSettlementWithValidation(
            attendees = attendees,
            absentees = absentees,
            earnedPoint = earnedPoint,
            promiseId = promise.id,
            deposit = promise.deposit,
        )

        return PromiseSettlementResponseDto(
            promise.id,
            promise.name,
            promise.deposit,
            earnedPoint,
            attendees.size,
            absentees.size,
        )
    }

    private fun validateAndGetParticipants(
        promise: Promise,
        memberId: Long,
    ): List<PromiseRole> {
        if (promise.status != PromiseStatus.READY) {
            throw BadRequestException("이미 정산된 약속이거나 아직 진행 중인 약속입니다.")
        }

        val participants = promiseRoleService.getParticipantsByStatus(promise.id, ParticipantStatus.READY)
        val organizer =
            participants.find { it.role == RoleStatus.ORGANIZER }
                ?: throw IllegalStateException("모임장이 존재하지 않습니다.")

        if (organizer.member.id != memberId) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "모임장만 정산을 진행할 수 있습니다.")
        }

        return participants
    }

    private fun processSettlementWithValidation(
        attendees: List<PromiseRole>,
        absentees: List<PromiseRole>,
        earnedPoint: Long,
        promiseId: Long,
        deposit: Int,
    ) {
        val cancelResults =
            attendees.map { attendee ->
                try {
                    // TODO 실제 결제가 이루어저야 해당 로직이 동작 가능함
                    // paymentService.cancelPayment(promiseId, attendee.member.id)
                    val paymentLog = paymentService.findByMemberIdAndPromiseId(attendee.member.id, promiseId)
                    paymentLog.setPaymentStatus(PaymentStatus.CANCELED)
                    true
                } catch (e: Exception) {
                    log.error("Failed to cancel payment for member ${attendee.member.id}: ${e.message}")
                    throw IllegalStateException("결제 취소 실패로 인해 정산을 진행할 수 없습니다: ${e.message}")
                }
            }

        if (cancelResults.all { it }) {
            attendees.forEach { attendee ->
                try {
                    settlePoint(earnedPoint, attendee.member, promiseId)
                } catch (e: Exception) {
                    log.error("Failed to settle points for member ${attendee.member.id}: ${e.message}")
                    throw IllegalStateException("포인트 정산 실패: ${e.message}")
                }
            }
            absentees.forEach { fineLogService.saveFineLog(deposit.toLong(), it.member.id, promiseId) }
        }
    }

    fun calculateEarnedPoint(
        attendees: List<PromiseRole>,
        absentees: List<PromiseRole>,
        deposit: Int,
    ): Long {
        val totalPoint = absentees.size * deposit.toLong()
        return totalPoint / attendees.size
    }

    fun settlePoint(
        earnedPoint: Long,
        member: Member,
        promiseId: Long,
    ) {
        pointLogService.savePointLog(PointLogStatus.PLUS, earnedPoint, member.id, promiseId)
        member.updatePoint(earnedPoint)
    }

    @Transactional
    fun cancelPromise(
        memberId: Long,
        promiseId: Long,
    ) {
        val promiseRole = promiseRoleService.getPromiseRole(memberId, promiseId)
        if (promiseRole.role == RoleStatus.ORGANIZER) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "모임장은 약속을 취소할 수 없습니다.")
        }

        try {
            // TODO 실제 결제가 가능할때 동작
            // paymentService.cancelPayment(promiseId, memberId)
            val paymentLog = paymentService.findByMemberIdAndPromiseId(memberId, promiseId)
            paymentLog.setPaymentStatus(PaymentStatus.CANCELED)
            promiseRole.updateStatus(ParticipantStatus.CANCELED)
        } catch (e: Exception) {
            throw BaseException(ErrorCode.IMAGE_EXTENSION_ERROR)
        }
    }

    private fun updateAndSplitParticipants(
        participants: List<PromiseRole>,
        attendedMemberIds: List<Long>,
    ): Pair<List<PromiseRole>, List<PromiseRole>> =
        participants
            .map { participant ->
                val newStatus =
                    if (attendedMemberIds.contains(participant.member.id)) {
                        ParticipantStatus.ATTENDED
                    } else {
                        ParticipantStatus.NOT_ATTENDED
                    }
                participant.updateStatus(newStatus)
                participant
            }.partition { participant ->
                participant.status == ParticipantStatus.ATTENDED
            }

    @Transactional
    fun deletePromise(
        memberId: Long,
        promiseId: Long,
    ) {
        val promiseRole = promiseRoleService.getPromiseRole(memberId, promiseId)
        if (promiseRole.role != RoleStatus.ORGANIZER) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "모임장만 약속을 삭제할 수 있습니다.")
        }
        val participants = promiseRoleService.getParticipantsByStatus(promiseId, ParticipantStatus.READY)
        try {
            participants.forEach { participant ->
                paymentService.cancelPayment(participant.member.id, promiseId)
            }
        } catch (e: Exception) {
            log.error("Failed to cancel payments: ${e.message}")
            throw BadRequestException("결제 취소 실패로 인해 약속 삭제를 진행할 수 없습니다")
        }

        participants.forEach { participant ->
            participant.updateStatus(ParticipantStatus.CANCELED)
        }

        promiseRole.promise.updateStatus(PromiseStatus.CANCELED)
        promiseRole.promise.delete()
    }
}
