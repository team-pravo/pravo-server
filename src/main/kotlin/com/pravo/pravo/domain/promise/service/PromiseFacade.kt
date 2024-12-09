package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.member.service.MemberService
import com.pravo.pravo.domain.point.service.PointLogService
import com.pravo.pravo.domain.promise.dto.response.ParticipantResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.model.enums.ParticipantStatus
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus
import com.pravo.pravo.domain.promise.model.enums.RoleStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PromiseFacade(
    private val promiseService: PromiseService,
    private val promiseRoleService: PromiseRoleService,
    private val memberService: MemberService,
    private val pointLogService: PointLogService,
) {
    @Transactional
    fun joinPromise(
        memberId: Long,
        promiseId: Long,
    ): PromiseResponseDto {
        if (promiseRoleService.checkPromiseRole(memberId, promiseId)) {
            return PromiseResponseDto.of(promiseService.getPromise(promiseId))
        }
        val promise = promiseService.getPromise(promiseId)
        val member = memberService.getMemberById(memberId)
        promiseRoleService.createPendingPromiseRole(member, promise, RoleStatus.PARTICIPANT)
        return PromiseResponseDto.of(promise)
    }

    @Transactional
    fun changePendingStatus(
        memberId: Long,
        promiseId: Long,
    ) {
        val promise = promiseService.getPromise(promiseId)
        promise.updateStatus(PromiseStatus.READY)

        val promiseRole = promiseRoleService.getPromiseRole(memberId, promiseId)
        promiseRole.updateStatus(ParticipantStatus.READY)
    }

    fun getPromiseDetailByMember(
        memberId: Long,
        promiseId: Long,
    ): PromiseDetailResponseDto {
        val promise =
            promiseService.getPromiseWithFetch(promiseId)
        val participants =
            promise.promiseRoles
                .filter { it.status != ParticipantStatus.PENDING }
                .map {
                    ParticipantResponseDto.of(it, it.member)
                }
        val settlementAmount =
            when (promise.status) {
                PromiseStatus.COMPLETED -> pointLogService.calculateSettlementAmount(memberId, promiseId, promise.deposit)
                else -> null
            }
        return PromiseDetailResponseDto.of(promise, participants, settlementAmount)
    }
}
