package com.pravo.pravo.domain.promise.service

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

        // 모든 PromiseRole 조회 후 개별 delete
        val roles = promiseRoleRepository.findAllByPromiseId(promiseId)
        roles.forEach { it.delete() }

        // Promise 삭제
        promiseRole.promise.delete()
    }
}
