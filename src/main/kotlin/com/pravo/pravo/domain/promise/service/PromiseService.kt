package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.promise.dto.response.ParticipantResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.repository.PromiseRepository
import com.pravo.pravo.domain.promise.repository.PromiseRepositoryImpl
import com.pravo.pravo.global.common.error.ErrorCode
import com.pravo.pravo.global.common.error.exception.NotFoundException
import jakarta.transaction.Transactional
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
    ): List<PromiseResponseDto> {
        return promiseRepository.getPromisesByMemberIdAndStartedAtAndEndedAt(memberId, startedAt, endedAt)
    }

    fun getPromiseDetailByMember(
        memberId: Long,
        promiseId: Long,
    ): PromiseDetailResponseDto {
        val promise =
            promiseRepository.getPromiseById(promiseId)
                ?: throw NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다")

        val participants =
            promise.promiseRoles.map {
                ParticipantResponseDto.of(it, it.member)
            }
        return PromiseDetailResponseDto.of(promise, participants)
    }

    @Transactional
    fun deletePromise(
        memberId: Long,
        promiseId: Long,
    ) {
        val promise =
            promiseRepository.getPromiseById(promiseId)
                ?: throw NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다")

        if (promise.organizer.id != memberId) {
            throw NotFoundException(ErrorCode.NOT_FOUND, "약속을 삭제할 권한이 없습니다")
        }

        promiseRepository.deletePromiseById(promise.id)
    }
}
