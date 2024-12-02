package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.member.model.Member
import com.pravo.pravo.domain.promise.model.Promise
import com.pravo.pravo.domain.promise.model.PromiseRole
import com.pravo.pravo.domain.promise.model.enums.ParticipantStatus
import com.pravo.pravo.domain.promise.model.enums.RoleStatus
import com.pravo.pravo.domain.promise.repository.PromiseRoleRepository
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PromiseRoleService(
    private val promiseRoleRepository: PromiseRoleRepository,
) {
    fun createPendingPromiseRole(
        member: Member,
        promise: Promise,
        role: RoleStatus,
    ) {
        val promiseRole = PromiseRole.pendingOf(promise, member, role)
        promiseRoleRepository.save(promiseRole)
    }

    fun checkPromiseRole(
        memberId: Long,
        promiseId: Long,
    ): Boolean {
        return promiseRoleRepository.existsByPromiseIdAndMemberId(promiseId, memberId)
    }

    @Transactional
    fun changePendingStatus(
        memberId: Long,
        promiseId: Long,
    ) {
        val promiseRole =
            promiseRoleRepository.findByPromiseIdAndMemberId(promiseId, memberId).orElseThrow {
                NotFoundException(ErrorCode.BAD_REQUEST, "약속을 찾을 수 없습니다")
            }
        promiseRole.updateStatus(ParticipantStatus.READY)
    }

    fun getPromiseRole(
        memberId: Long,
        promiseId: Long,
    ): PromiseRole {
        return promiseRoleRepository.findByPromiseIdAndMemberId(promiseId, memberId).orElseThrow {
            NotFoundException(ErrorCode.BAD_REQUEST, "약속을 찾을 수 없습니다")
        }
    }

    fun getParticipantsByStatus(
        promiseId: Long,
        status: ParticipantStatus,
    ): List<PromiseRole> {
        return promiseRoleRepository.findByPromiseIdAndStatus(promiseId, status)
    }
}
