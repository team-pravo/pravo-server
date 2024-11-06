package com.pravo.pravo.domain.promise.dto.response

import com.pravo.pravo.domain.promise.model.Promise
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class PromiseResponseDto(
    @Schema(description = "약속 ID", example = "1")
    val id: Long,
    @Schema(description = "약속 이름", example = "약속 이름")
    val name: String,
    @Schema(description = "약속 일시", example = "2021-08-01T00:00:00")
    val promiseDate: LocalDateTime,
    @Schema(description = "약속 장소", example = "약속 장소")
    val location: String?,
    @Schema(description = "약속 상태", example = "READY")
    val status: PromiseStatus,
) {
    companion object {
        fun of(promise: Promise): PromiseResponseDto {
            return PromiseResponseDto(
                id = promise.id,
                name = promise.name,
                promiseDate = promise.promiseDate,
                location = promise.location,
                status = promise.status,
            )
        }
    }
}
