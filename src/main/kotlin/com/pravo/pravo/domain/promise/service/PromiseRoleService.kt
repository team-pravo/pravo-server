package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.member.model.Member
import com.pravo.pravo.domain.promise.model.Promise
import com.pravo.pravo.domain.promise.model.PromiseRole
import com.pravo.pravo.domain.promise.repository.PromiseRoleRepository
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.UnauthorizedException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PromiseRoleService(
    private val promiseRoleRepository: PromiseRoleRepository,
) {
    @Transactional
    fun deletePromise(
        memberId: Long,
        promiseId: Long,
    ) {
        val promiseRole =
            promiseRoleRepository.findByPromiseIdAndMemberId(promiseId, memberId)
                ?.takeIf { it.isOrganizer }
                ?: throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "약속을 삭제할 권한이 없습니다")

        promiseRole.promise.delete()
    }

    fun createPromiseRole(
        member: Member,
        promise: Promise,
    ) {
        val promiseRole = PromiseRole.pendingOf(promise, member)
        promiseRoleRepository.save(promiseRole)
    }

    fun checkPromiseRole(
        memberId: Long,
        promiseId: Long,
    ): Boolean {
        return promiseRoleRepository.findByPromiseIdAndMemberId(promiseId, memberId) != null
    }

    @Transactional
    fun cancelPromise(
        memberId: Long,
        promiseId: Long,
    ) {
        val promiseRole =
            promiseRoleRepository.findByPromiseIdAndMemberId(promiseId, memberId)
                ?.takeIf { !it.isOrganizer }
                ?: throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "약속을 취소할 권한이 없습니다")

        // TODO: 수수료 부과 로직 추가
        promiseRole.delete()
    }
}
