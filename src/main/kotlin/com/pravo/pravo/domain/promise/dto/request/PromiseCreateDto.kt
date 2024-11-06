package com.pravo.pravo.domain.promise.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class PromiseCreateDto(
    @Schema(description = "약속 이름", example = "약속 이름")
    val name: String,
    @Schema(description = "약속 일시", example = "2021-08-01T00:00:00")
    val promiseDate: LocalDateTime,
    @Schema(description = "약속 장소", example = "약속 장소")
    val location: String,
)
