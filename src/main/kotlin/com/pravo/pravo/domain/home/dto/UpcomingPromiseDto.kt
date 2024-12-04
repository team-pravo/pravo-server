package com.pravo.pravo.domain.home.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class UpcomingPromiseDto(
    @Schema(description = "약속 일시", example = "2021-08-01T00:00:00")
    val scheduledAt: LocalDateTime,
    @Schema(description = "약속 이름", example = "약속 이름")
    val name: String,
)
