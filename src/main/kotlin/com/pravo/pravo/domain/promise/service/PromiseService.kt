package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.promise.dto.request.PromiseCreateDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.model.Promise
import com.pravo.pravo.domain.promise.repository.PromiseRepository
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PromiseService(
    private val promiseRepository: PromiseRepository,
) {
    fun getPromisesByMember(
        memberId: Long,
        startedAt: LocalDate?,
        endedAt: LocalDate?,
    ): List<PromiseResponseDto> = promiseRepository.getPromisesByMemberIdAndStartedAtAndEndedAt(memberId, startedAt, endedAt)

    fun createPendingPromise(promiseCreateDto: PromiseCreateDto): Promise {
        val promise = Promise.pendingOf(promiseCreateDto)
        return promiseRepository.save(promise)
    }

    fun getPromise(promiseId: Long): Promise {
        return promiseRepository.findById(promiseId)
            .orElseThrow { NotFoundException(ErrorCode.BAD_REQUEST, "약속을 찾을 수 없습니다") }
    }

    fun getPromiseWithFetch(promiseId: Long): Promise {
        return promiseRepository.getPromiseById(promiseId)
            .orElseThrow { NotFoundException(ErrorCode.BAD_REQUEST, "약속을 찾을 수 없습니다") }
    }
}
