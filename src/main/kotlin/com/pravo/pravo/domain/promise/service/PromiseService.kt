package com.pravo.pravo.domain.promise.service

import com.pravo.pravo.domain.member.service.MemberService
import com.pravo.pravo.domain.promise.dto.response.ParticipantResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.repository.PromiseRepository
import com.pravo.pravo.domain.promise.repository.PromiseRoleRepository
import com.pravo.pravo.global.common.error.ErrorCode
import com.pravo.pravo.global.common.error.exception.NotFoundException
import com.pravo.pravo.global.common.error.exception.UnauthorizedException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PromiseService(
    private val promiseRepository: PromiseRepository,
    private val promiseRoleRepository: PromiseRoleRepository,
    private val memberService: MemberService,
) {
    fun getPromisesByMember(
        memberId: Long,
        startedAt: LocalDate?,
        endedAt: LocalDate?,
    ): List<PromiseResponseDto> {
        memberService.validateMemberById(memberId)
        return promiseRepository.getPromisesByMemberIdAndStartedAtAndEndedAt(memberId, startedAt, endedAt)
            .map(PromiseResponseDto::of)
    }

    fun getPromiseDetailByMember(
        memberId: Long,
        promiseId: Long,
    ): PromiseDetailResponseDto {
        memberService.validateMemberById(memberId)
        val promise =
            promiseRepository.getPromiseById(promiseId)
                ?: throw NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다")

        promise.promiseRoles
            .find { it.member.id == memberId }
            ?: throw UnauthorizedException(ErrorCode.UNAUTHORIZED, "약속 참가자가 아닙니다")

        val participants =
            promise.promiseRoles.map {
                ParticipantResponseDto.of(it, it.member)
            }
        return PromiseDetailResponseDto.of(promise, participants)
    }
}
