package com.pravo.pravo.domain.promise.dto.response

import com.pravo.pravo.domain.promise.model.Promise
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus
import io.swagger.v3.oas.annotations.media.Schema

data class PromiseDetailResponseDto(
    @Schema(description = "약속 ID", example = "1")
    val id: Long,
    @Schema(description = "약속 이름", example = "약속 이름")
    val name: String,
    @Schema(description = "약속 일시", example = "2021-08-01T00:00:00")
    val promiseDate: String,
    @Schema(description = "약속 장소", example = "약속 장소")
    val location: String?,
    @Schema(description = "약속 상태", example = "READY")
    val status: PromiseStatus,
    @Schema(description = "예약금", example = "10000")
    val deposit: Int,
    @Schema(description = "참가자", example = "참가자")
    val participants: List<ParticipantResponseDto>,
) {
    companion object {
        fun of(
            promise: Promise,
            participants: List<ParticipantResponseDto>,
        ): PromiseDetailResponseDto {
            return PromiseDetailResponseDto(
                id = promise.id,
                name = promise.name,
                promiseDate = promise.promiseDate.toString(),
                location = promise.location,
                status = promise.status,
                deposit = promise.deposit,
                participants = participants,
            )
        }
    }
}
