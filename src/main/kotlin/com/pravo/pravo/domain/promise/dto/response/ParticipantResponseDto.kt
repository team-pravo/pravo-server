package com.pravo.pravo.domain.promise.dto.response

import com.pravo.pravo.domain.member.model.Member
import com.pravo.pravo.domain.promise.model.PromiseRole
import com.pravo.pravo.domain.promise.model.enums.ParticipantStatus
import com.pravo.pravo.domain.promise.model.enums.RoleStatus
import io.swagger.v3.oas.annotations.media.Schema

data class ParticipantResponseDto(
    @Schema(description = "참가자 ID", example = "1")
    val id: Long,
    @Schema(description = "참가자 이름", example = "참가자 이름")
    val name: String,
    @Schema(description = "참가자 프로필 이미지", example = "참가자 프로필 이미지")
    val profileImageUrl: String?,
    @Schema(description = "참가자 상태", example = "READY")
    val status: ParticipantStatus,
    @Schema(description = "참가자 역할", example = "OWNER")
    val role: RoleStatus,
) {
    companion object {
        fun of(
            promiseRole: PromiseRole,
            participant: Member,
        ): ParticipantResponseDto {
            return ParticipantResponseDto(
                id = participant.id,
                name = participant.name,
                profileImageUrl = participant.profileImageUrl,
                status = promiseRole.status,
                role = promiseRole.role,
            )
        }
    }
}
