package com.pravo.pravo.domain.promise.dto.response

import com.pravo.pravo.domain.promise.model.enums.PromiseStatus
import com.querydsl.core.annotations.QueryProjection
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class PromiseResponseDto
    @QueryProjection
    constructor(
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
        @Schema(description = "모임장 이름", example = "모임장 이름")
        val organizerName: String,
        @Schema(description = "모임장 프로필 이미지", example = "모임장 프로필 이미지")
        val organizerProfileImageUrl: String?,
    )
