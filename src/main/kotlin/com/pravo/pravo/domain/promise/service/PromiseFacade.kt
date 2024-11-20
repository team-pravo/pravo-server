package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.member.service.MemberService
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.model.enums.RoleStatus
import org.springframework.stereotype.Service

@Service
class PromiseFacade(
    private val promiseService: PromiseService,
    private val promiseRoleService: PromiseRoleService,
    private val memberService: MemberService,
) {
    fun joinPromise(
        memberId: Long,
        promiseId: Long,
    ): PromiseResponseDto {
        val promiseRole = promiseRoleService.checkPromiseRole(memberId, promiseId)
        if (promiseRole) {
            return PromiseResponseDto.of(promiseService.getPromise(promiseId))
        }
        val promise = promiseService.getPromise(promiseId)
        val member = memberService.getMemberById(memberId)
        promiseRoleService.createPendingPromiseRole(member, promise, RoleStatus.PARTICIPANT)
        return PromiseResponseDto.of(promise)
    }
}
