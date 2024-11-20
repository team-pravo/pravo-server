package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.promise.dto.request.PromiseCreateDto
import com.pravo.pravo.domain.promise.dto.response.ParticipantResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
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

    fun getPromiseDetailByMember(
        memberId: Long,
        promiseId: Long,
    ): PromiseDetailResponseDto {
        val promise =
            promiseRepository.getPromiseById(promiseId).orElseThrow {
                NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다")
            }
        val participants =
            promise.promiseRoles.map {
                ParticipantResponseDto.of(it, it.member)
            }
        return PromiseDetailResponseDto.of(promise, participants)
    }

    fun createPendingPromise(promiseCreateDto: PromiseCreateDto): Promise {
        val promise = Promise.pendingOf(promiseCreateDto)
        return promiseRepository.save(promise)
    }

    fun changePendingStatus(promiseId: Long) {
        val promise =
            promiseRepository.findById(promiseId).orElseThrow {
                NotFoundException(ErrorCode.NOT_FOUND)
            }
        promise.changePendingStatus()
    }

    fun getPromise(promiseId: Long): Promise {
        return promiseRepository.findById(promiseId)
            .orElseThrow { NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다") }
    }
}
